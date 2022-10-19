package com.pms.service;

import com.pms.comm.CommService;
import com.pms.comm.ncp.service.SmsService;
import com.pms.domain.Place;
import com.pms.domain.Room;
import com.pms.domain.booking.Booking;
import com.pms.domain.booking.BookingState;
import com.pms.domain.user.User;
import com.pms.dto.booking.ReturnBookingDTO;
import com.pms.repository.BookingRepository;
import com.pms.repository.PlaceRepository;
import com.pms.repository.RoomRepository;
import com.pms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService extends CommService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    private final String SECRET_KEY = "test_sk_5mBZ1gQ4YVXzygzAM0a8l2KPoqNb:";

    byte[] targetBytes = SECRET_KEY.getBytes();
    Base64.Encoder encoder = Base64.getEncoder();
    byte[] encodedBytes = encoder.encode(targetBytes);
    String toss_key = "Basic " + new String(encodedBytes);

    public Booking insertOrUpdateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new NullPointerException("NOT EXIST BOOKING DATA"));
    }

    public List<List<ReturnBookingDTO>> getReturnBookingData(LocalDate startDt, LocalDate endDt) {
        List<List<ReturnBookingDTO>> finalList = new ArrayList<>();
        Period period = Period.between(startDt, endDt);

        List<Booking> bookingList = bookingRepository.findByRegistDtBetween(startDt, endDt);
        List<ReturnBookingDTO> returnDtoList = new ArrayList<>();
        if (bookingList.isEmpty())
            return finalList;

        bookingList.forEach((booking) -> {
            Place place = placeRepository.findByPlaceId(booking.getPlaceId())
                    .orElseThrow(() -> new NullPointerException("NOT FOUND PLACE"));
            Room room = roomRepository.findByRoomId(booking.getRoomId())
                    .orElseThrow(() -> new NullPointerException("NOT FOUND ROOM"));

            returnDtoList.add(ReturnBookingDTO.builder()
                    .bookingId(booking.getBookingId())
                    .reservedName(booking.getReservedName())
                    .userPhoneNo(booking.getReservedPhoneNo())
                    .placeName(place.getName())
                    .roomName(room.getName())
                    .startDt(booking.getStartDt())
                    .endDt(booking.getEndDt())
                    .bookingState(booking.getBookingState())
                    .registDt(booking.getRegistDt())
                    .build());
        });

        for (int i = 0; i < period.getDays() + 1; i++) {
            LocalDate date = startDt.plusDays(i);
            List<ReturnBookingDTO> dateList = new ArrayList<>();
            returnDtoList.forEach(dto -> {
                if (dto.getRegistDt().isEqual(date))
                    dateList.add(dto);
            });

            finalList.add(dateList);
        }

        return finalList;
    }

    public boolean cancelBooking(String paymentKey, String refund){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", toss_key)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객이 취소를 원함\",\"cancelAmount\":" + refund + "}"))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return true;
        } catch (Exception e){
            log.warn("Refund error");
        }
        return false;
    }
}
