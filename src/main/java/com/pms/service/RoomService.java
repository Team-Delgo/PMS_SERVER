package com.pms.service;


import com.pms.domain.Room;
import com.pms.domain.price.Price;
import com.pms.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    // RoomId로 Room 조회
    public Room getRoomByRoomId(int roomId) {
        return roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ROOM"));
    }
}
