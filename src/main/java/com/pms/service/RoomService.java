package com.pms.service;


import com.pms.domain.Room;
import com.pms.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    // RoomId로 Room 조회
    public Room getRoomById(int roomId) {
        return roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ROOM"));
    }
}
