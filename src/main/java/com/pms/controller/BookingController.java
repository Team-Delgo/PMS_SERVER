package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.CommService;
import com.pms.domain.booking.Booking;
import com.pms.domain.booking.BookingState;
import com.pms.dto.ResponseDTO;
import com.pms.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pms/booking")
public class BookingController extends CommController {

    private final CommService commService;
    private final BookingService bookingService;

    /**
     * 예약 내용 조회 : PMS
     */
    @GetMapping("/getData")
    public ResponseEntity getBookingList(
            @RequestParam String startDt,
            @RequestParam String endDt) {
        return SuccessReturn(bookingService.getReturnBookingData(LocalDate.parse(startDt), LocalDate.parse(endDt)));
    }

    /**
     * 예약 확정 : PMS
     */
    @PostMapping(value = {"/confirm/{bookingId}", "/confirm"})
    public ResponseEntity bookingConfirm(@PathVariable String bookingId) {
        // TODO: booking_status - F[fix] 로 변경
        Booking booking = bookingService.getBookingByBookingId(bookingId);

        if (booking.getBookingState().equals(BookingState.W)) {
            booking.setBookingState(BookingState.F);
            bookingService.insertOrUpdateBooking(booking);
        }

        // TODO: [예약 성공 ]
        // TODO: TOSS 결제 취소 진행
        // TODO: User에게 예약확정문자 발송

        // TODO: [예약 실패 ]
        // TODO: User에게 예약실패문자 발송

        return SuccessReturn();
    }

    /**
     * 취소 확정 : PMS
     */
    @PostMapping(value = {"/cancel/confirm/{bookingId}", "/cancel/confirm"})
    public ResponseEntity cancelConfirm(@PathVariable String bookingId) {

        Booking booking = bookingService.getBookingByBookingId(bookingId);
        booking.setBookingState(BookingState.CF);
        bookingService.insertOrUpdateBooking(booking);
        // TODO: TOSS 취소

        // TODO: User에게 취소문자 발송

        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(200).codeMsg("Success").data("").build()
        );
    }
}