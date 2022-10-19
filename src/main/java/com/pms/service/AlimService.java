package com.pms.service;

import com.pms.comm.ncp.service.AlimTalkService;
import com.pms.domain.Cancel;
import com.pms.domain.Place;
import com.pms.domain.Room;
import com.pms.domain.booking.Booking;
import com.pms.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlimService {
    private final AlimTalkService alimTalkService;
    private final PlaceService placeService;
    private final UserService userService;
    private final RoomService roomService;
    private final CancelService cancelService;

    public void sendFixAlimTalk(Booking booking) throws IOException {
        Place place = placeService.getPlaceByPlaceId(booking.getPlaceId());
        Room room = roomService.getRoomByRoomId(booking.getRoomId());
        User user = userService.getUserByUserId(booking.getUserId());
        try {
            String content = "[Delgo] 예약확정 안내\n" +
                    "안녕하세요? " + booking.getReservedName() + "님의 예약이 확정되었습니다.\n" +
                    "아래 숙소의 예약 정보를 확인해주세요.\n" +
                    "\n" +
                    "숙소이름 : " + place.getName() + "\n" +
                    "객실타입 : " + room.getName() + " (기준인원" + room.getPersonStandardNum() + "명)\n" +
                    "입실일시: " + booking.getStartDt() + " " + place.getCheckin() + " ~\n" +
                    "퇴실일시: " + booking.getEndDt() + " " + place.getCheckout() + "\n" +
                    "\n" +
                    "반려견과 좋은 시간 보내세요!";
            alimTalkService.sendAlimTalk("FixAlim", user.getPhoneNo(), content);
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public void sendCancelAlimTalk(Booking booking) throws IOException {
        Place place = placeService.getPlaceByPlaceId(booking.getPlaceId());
        Room room = roomService.getRoomByRoomId(booking.getRoomId());
        User user = userService.getUserByUserId(booking.getUserId());
        Period period = Period.between(LocalDate.now(), booking.getStartDt());
        int commission = 0;

        if (period.getDays() <= 14){
            Cancel cancel = cancelService.getCancelByPlaceIdAndRemainDay(booking.getPlaceId(), period.getDays());
            commission = booking.getFinalPrice() / 100 * (100 - cancel.getReturnRate());
        }
        int refundPrice = booking.getFinalPrice() - commission;

        try {
            String content = "[Delgo] 예약취소 안내\n" +
                    "안녕하세요? Delgo입니다.\n" +
                    "고객님께서 예약하신 숙소가 취소되었습니다.\n" +
                    "\n" +
                    "숙소이름 : " + place.getName() + "\n" +
                    "객실타입 : " + room.getName() + " (기준인원 " + room.getPersonStandardNum() + " 명)\n" +
//                    "환불예정금액: " + price + "원\n" +
                    "환불예정금액: " + refundPrice + "원\n" +
                    "결제수단: " + "신용카드" + "\n" +
                    "환불소요기간: 영업일 기준 3~5일\n" +
                    "\n" +
                    "이용해 주셔서 감사합니다.";
            alimTalkService.sendAlimTalk("Cancel", user.getPhoneNo(), content);
        } catch (Exception e) {
            throw new IOException();
        }
    }

}
