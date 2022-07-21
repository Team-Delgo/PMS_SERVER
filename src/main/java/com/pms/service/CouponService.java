package com.pms.service;


import com.pms.comm.CommService;
import com.pms.domain.coupon.CouponManager;
import com.pms.repository.CouponManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CouponService extends CommService {
    private final CouponManagerRepository couponManagerRepository;

    // ------------------------------------- Coupon Manager -------------------------------------
    public CouponManager insertOrUpdateCouponManager(CouponManager couponManager) {
        return couponManagerRepository.save(couponManager);
    }

    public List<CouponManager> getAllList() {
        return couponManagerRepository.findAll();
    }

    public void deleteCouponManager(CouponManager couponManager) {
        couponManagerRepository.delete(couponManager);
    }

    public CouponManager getCouponManagerByCode(String couponCode) {
        return couponManagerRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new NullPointerException("WRONG COUPON CODE")); // ERROR: Coupon Code 잘못된 입력
    }

    public CouponManager getCouponManagerById(int couponManagerId) {
        return couponManagerRepository.findByCouponManagerId(couponManagerId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND COUPON MANAGER")); // ERROR: Coupon Code 잘못된 입력
    }

}
