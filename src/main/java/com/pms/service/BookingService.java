package com.pms.service;

import com.pms.comm.CommService;
import com.pms.domain.booking.Booking;
import com.pms.dto.booking.BookingResDTO;
import com.pms.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService extends CommService {

    // Service
    private final PlaceService placeService;
    private final RoomService roomService;

    // Repository
    private final BookingRepository bookingRepository;

    public Booking register(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new NullPointerException("NOT EXIST BOOKING DATA"));
    }

    public List<List<BookingResDTO>> getBooking(LocalDate startDt, LocalDate endDt) {
        List<List<BookingResDTO>> dateList = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findByRegistDtBetween(startDt, endDt);

        if (bookingList.isEmpty())
            return dateList;

        List<BookingResDTO> resDTOs = bookingList.stream().map(booking -> booking.toResDTO(
                        placeService.getPlaceById(booking.getPlaceId()),
                        roomService.getRoomById(booking.getRoomId())))
                .collect(Collectors.toList());

        for (int i = 0; i < Period.between(startDt, endDt).getDays() + 1; i++) {
            LocalDate date = startDt.plusDays(i);
            dateList.add(resDTOs.stream()
                    .filter(dto -> dto.getRegistDt().isEqual(date))
                    .collect(Collectors.toList()));
        }

        return dateList;
    }

//    public boolean cancelBooking(String paymentKey, String refund){
//        String SECRET_KEY = "test_sk_5mBZ1gQ4YVXzygzAM0a8l2KPoqNb:";
//
//        byte[] targetBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
//        String toss_key = "Basic " + new String(targetBytes);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
//                .header("Authorization", toss_key)
//                .header("Content-Type", "application/json")
//                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객이 취소를 원함\",\"cancelAmount\":" + refund + "}"))
//                .build();
//
//        try {
//            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers
//            .ofString());
//            System.out.println(response.body());
//            return true;
//        } catch (Exception e){
//            log.warn("Refund error");
//        }
//        return false;
//    }
}
