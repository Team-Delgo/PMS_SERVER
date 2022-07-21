package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.exception.ApiCode;
import com.pms.domain.coupon.Coupon;
import com.pms.domain.coupon.CouponManager;
import com.pms.dto.CouponManagerDTO;
import com.pms.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/manage")
public class CouponController extends CommController {

    private final CouponService couponService;

    // TODO: C
    // 쿠폰 발행 API [ 관리자 ]
    @PostMapping("/regist")
    public ResponseEntity registCouponManager(@Validated @RequestBody CouponManagerDTO couponManagerDTO) {
        couponService.insertOrUpdateCouponManager(couponManagerDTO.build());
        return SuccessReturn();
    }

    // TODO: getData ( select 조건으로 뭐가 있을까? ) ( 제한 날짜? )
    @GetMapping("/getData")
    public ResponseEntity getCouponManager() {
        return SuccessReturn(couponService.getAllList());
    }
    // TODO: U (굳이?)

    // TODO: D
    @PostMapping(value = {"/delete/{couponManagerId}", "/delete"})
    public ResponseEntity deleteCouponManager(@PathVariable Integer couponManagerId) {
        CouponManager couponManager = couponService.getCouponManagerById(couponManagerId);
        couponService.deleteCouponManager(couponManager);
        return SuccessReturn();
    }

}