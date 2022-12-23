package com.pms.service;


import com.pms.comm.CommService;
import com.pms.domain.coupon.CouponManager;
import com.pms.repository.CouponManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CouponService extends CommService {

    private final CouponManagerRepository couponManagerRepository;

    public CouponManager register(CouponManager couponManager) {
        return couponManagerRepository.save(couponManager);
    }

    public List<CouponManager> getAll() {
        return couponManagerRepository.findAll();
    }

    public void delete(CouponManager couponManager) {
        couponManagerRepository.delete(couponManager);
    }

    public CouponManager getCouponManagerById(int couponManagerId) {
        return couponManagerRepository.findByCouponManagerId(couponManagerId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND COUPON MANAGER")); // ERROR: Coupon Code 잘못된 입력
    }

}
