package com.pms.repository;


import com.pms.domain.price.Price;
import com.pms.domain.price.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    List<Price> findByPriceDateBetween(String startDate, String endDate);
}
