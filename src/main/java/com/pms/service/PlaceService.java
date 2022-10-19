package com.pms.service;

import com.pms.domain.Place;
import com.pms.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    // PlaceId로 Place 조회
    public Place getPlaceByPlaceId(int placeId) {
        return placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND PLACE"));
    }
}
