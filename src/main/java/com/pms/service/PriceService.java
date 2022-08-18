package com.pms.service;


import com.pms.comm.CommService;
import com.pms.domain.price.Price;
import com.pms.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService extends CommService {

    private final PriceRepository priceRepository;

    public void changeToReserveWait(LocalDate startDt, LocalDate endDt, int isWait) {
        List<Price> priceList = priceRepository.findByPriceDateBetween(startDt.toString(), endDt.toString());
        priceList.forEach(price -> {
            price.setIsWait(isWait);
            priceRepository.save(price);
        });
    }

    public void changeToReserveFix(LocalDate startDt, LocalDate endDt, int isBooking) {
        List<Price> priceList = priceRepository.findByPriceDateBetween(startDt.toString(), endDt.toString());
        priceList.forEach(price -> {
            price.setIsBooking(isBooking);
            priceRepository.save(price);
        });
    }
}
