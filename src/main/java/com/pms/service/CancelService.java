package com.pms.service;


import com.pms.comm.CommService;
import com.pms.domain.Cancel;
import com.pms.repository.CancelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CancelService extends CommService {

    private final CancelRepository cancelRepository;

    public Cancel getCancelByPlaceIdAndRemainDay(int placeId, int remainDay) {
        log.info("placeId : {} , remainDay  : {}",placeId,remainDay);
        return cancelRepository.findByPlaceIdAndRemainDay(placeId, remainDay)
                .orElseThrow(() -> new NullPointerException("NOT FOUND CANCEL"));
    }

}
