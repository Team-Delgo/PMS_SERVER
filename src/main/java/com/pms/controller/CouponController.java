package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.dto.CouponManagerReqDTO;
import com.pms.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController extends CommController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity register(@Validated @RequestBody CouponManagerReqDTO reqDTO) {
        return SuccessReturn(couponService.register(reqDTO.toEntity()));
    }

    @GetMapping
    public ResponseEntity select() {
        return SuccessReturn(couponService.getAll());
    }

    @DeleteMapping("/{couponManagerId}")
    public ResponseEntity delete(@PathVariable Integer couponManagerId) {
        couponService.delete(couponService.getCouponManagerById(couponManagerId));

        return SuccessReturn();
    }

}