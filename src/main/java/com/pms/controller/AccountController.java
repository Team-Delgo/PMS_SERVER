package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.exception.ApiCode;
import com.pms.comm.security.jwt.JwtService;
import com.pms.comm.security.jwt.JwtToken;
import com.pms.comm.security.jwt.config.AccessTokenProperties;
import com.pms.comm.security.jwt.config.RefreshTokenProperties;
import com.pms.domain.Admin;
import com.pms.dto.AdminReqDTO;
import com.pms.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController extends CommController {

    private final JwtService jwtService;
    private final AdminService adminService;

    // 비밀번호 수정
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody AdminReqDTO reqDTO) {
        adminService.changePassword(reqDTO);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody AdminReqDTO reqDTO, HttpServletResponse response) {
        if(adminService.isEmailExisting(reqDTO.getEmail()))
            return ErrorReturn(ApiCode.EMAIL_DUPLICATE_ERROR);

        Admin admin = adminService.register(reqDTO);

        JwtToken jwt = jwtService.createToken(admin.getAdminId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(admin);
    }
}
