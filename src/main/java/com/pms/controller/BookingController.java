package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.exception.ApiCode;
import com.pms.comm.ncp.service.SmsService;
import com.pms.domain.booking.Booking;
import com.pms.domain.booking.BookingState;
import com.pms.service.AlimService;
import com.pms.service.BookingService;
import com.pms.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController extends CommController {

    private final String ADMIN_PHONE_NO = "01077652211";

    private final SmsService smsService;
    private final AlimService alimService;
    private final PriceService priceService;
    private final BookingService bookingService;

    /**
     * 예약 내용 조회 : PMS
     */
    @GetMapping
    public ResponseEntity getBookingList(@RequestParam String startDt, @RequestParam String endDt) {
        return SuccessReturn(bookingService.getBooking(LocalDate.parse(startDt), LocalDate.parse(endDt)));
    }

    /**
     * 예약 확정 API : PMS
     */
    @PostMapping(value = {"/confirm/{bookingId}", "/confirm"})
    public ResponseEntity bookingConfirm(@PathVariable String bookingId) {
        // booking_status - F[fix] 로 변경
        Booking booking = bookingService.register(bookingService.getBookingById(bookingId).setBookingState(BookingState.F));
        // Price: isBooking -> 1, isWait -> 0 변경
        priceService.changeToReserve(booking.getStartDt(), booking.getEndDt(), 0, 1);

        try {
            String msg = "예약번호 : " + booking.getBookingId() + " 예약확정 했습니다.";
            smsService.sendSMS(ADMIN_PHONE_NO, msg);  // 운영진에게 예약 요청 문자 발송
            alimService.sendFixAlimTalk(booking);  // 사용자에게 예약 확정 문자 발송
        } catch (Exception e) {
            return ErrorReturn(ApiCode.SMS_ERROR);
        }

        return SuccessReturn();
    }


    /**
     * 취소 확정 : PMS
     */
    @PostMapping(value = {"/cancel/confirm/{bookingId}", "/cancel/confirm"})
    public ResponseEntity cancelConfirm(@PathVariable String bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (!(booking.getBookingState() == BookingState.CW || booking.getBookingState() == BookingState.W))
            return ErrorReturn(ApiCode.BOOKING_CANCEL_STATE_ERROR);

        // booking_status - CF[Cancel Fix] 로 변경
        bookingService.register(booking.setBookingState(BookingState.CF));
        // Price: isWait -> 0 변경
        priceService.changeToReserve(booking.getStartDt(), booking.getEndDt(), 0, 0);  // Price: isBooking -> 1, isWait -> 0 변경

        try {
            String msg = "예약번호 : " + booking.getBookingId() + " 취소완료 했습니다.";
            smsService.sendSMS(ADMIN_PHONE_NO, msg);  // 운영진에게 예약 요청 문자 발송
            alimService.sendCancelAlimTalk(booking);  // 사용자에게 예약 확정 문자 발송
        } catch (Exception e) {
            return ErrorReturn(ApiCode.SMS_ERROR);
        }

        return SuccessReturn();
    }
}