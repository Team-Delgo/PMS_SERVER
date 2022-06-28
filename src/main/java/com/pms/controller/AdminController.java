package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.comm.security.jwt.Access_JwtProperties;
import com.pms.comm.security.jwt.Refresh_JwtProperties;
import com.pms.domain.Admin;
import com.pms.dto.SignUpDTO;
import com.pms.service.AdminService;
import com.pms.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController extends CommController {

    private final AdminService adminService;
    private final TokenService tokenService;

    // 비밀번호 수정
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Validated @RequestBody SignUpDTO signUpDTO) {
        String checkedEmail = signUpDTO.getEmail();
        String newPassword = signUpDTO.getPassword();

        adminService.changePassword(checkedEmail, newPassword);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpDTO signUpDTO, HttpServletResponse response) {
        String phoneNoUpdate = signUpDTO.getPhoneNo().replaceAll("[^0-9]", "");
        signUpDTO.setPhoneNo(phoneNoUpdate);

        Admin admin = adminService.signup(signUpDTO.getAdmin());
        admin.setPassword(""); // 보안

        String Access_jwtToken = tokenService.createToken("Access", Integer.toString(admin.getAdminId())); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", Integer.toString(admin.getAdminId())); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(admin);
    }

    // 회원탈퇴 //TODO: 권한체크 강하게 해야 함.
    @PostMapping(value = {"/delete/{adminId}", "/delete"})
    public ResponseEntity<?> deleteUser(@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return SuccessReturn();
    }
}
