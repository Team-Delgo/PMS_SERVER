package com.pms.repository;


import com.pms.domain.coupon.CouponManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponManagerRepository extends JpaRepository<CouponManager, Integer> {
    Optional<CouponManager> findByCouponCode(String couponCode);

    Optional<CouponManager> findByCouponManagerId(int couponManagerId);
}
