package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.CommService;
import com.pms.comm.exception.ApiCode;
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

    @GetMapping(value={"/getPaymentData/{bookingId}", "/getPaymentData"})
    public ResponseEntity getPaymentData(@PathVariable String bookingId){
        Booking booking = bookingService.getBookingByBookingId(bookingId);
        bookingService.getPaymentData(booking.getPaymentKey());
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
        String refund = "0";
        if(bookingService.cancelBooking(booking.getPaymentKey(), refund)){
            return ErrorReturn(ApiCode.BOOKING_CANCEL_ERROR);
        };
        
        // TODO: TOSS 취소 [추후 추가]
        try {
            // 운영진에게 예약 요청 문자 발송
            String adminMsg = "예약번호 : " + booking.getBookingId() + " 취소완료 했습니다.";
            smsService.sendSMS(ADMIN_PHONE_NO, adminMsg);
            // 사용자에게 예약 확정 문자 발송
            alimService.sendCancelAlimTalk(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorReturn(ApiCode.SMS_ERROR);
        }

        return SuccessReturn();
    }
}