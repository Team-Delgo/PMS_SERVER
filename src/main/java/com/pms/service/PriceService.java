package com.pms.service;


import com.pms.comm.CommService;
import com.pms.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService extends CommService {
    private final PriceRepository priceRepository;

    public void changeToReserve(LocalDate startDt, LocalDate endDt, int isWait, int isBooking) {
        priceRepository.saveAll(
                priceRepository.findByPriceDateBetween(startDt.toString(), endDt.toString()).stream()
                        .peek(price -> {
                            price.setIsBooking(isBooking);
                            price.setIsWait(isWait);
                        }).collect(Collectors.toList()));
    }
}
