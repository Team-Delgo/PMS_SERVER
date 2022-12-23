package com.pms.dto.booking;

import com.pms.domain.booking.BookingState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResDTO {
    private String bookingId; // 예약번호
    private String reservedName; // 유저 이름
    private String userPhoneNo; // 유저 핸드폰 번호
    private String placeName; // 숙소명
    private String roomName; // 룸타입 ( 이름 )
    private LocalDate startDt; // 체크인 날짜
    private LocalDate endDt; // 체크아웃 날짜
    private BookingState bookingState; // 현재 예약 상태 코드
    private LocalDate registDt;
}