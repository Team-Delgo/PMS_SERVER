package com.pms.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.pms.comm.CommController;
import com.pms.comm.exception.ApiCode;
import com.pms.comm.security.jwt.Access_JwtProperties;
import com.pms.comm.security.jwt.Refresh_JwtProperties;
import com.pms.domain.Admin;
import com.pms.service.TokenService;
import com.pms.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController extends CommController {

    private final AdminService adminService;
    private final TokenService tokenService;

    final String ACCESS = "Access";
    final String REFRESH = "Refresh";
    final String ADMIN_ID = "adminId";

    /*
     * Login 성공
     * Header [ Access_Token, Refresh_Token ]
     * Body [ User , Pet ]
     * 담아서 반환한다.
     */
    @PostMapping("/loginSuccess")
    public ResponseEntity<?> loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        log.info("들어옴");
        String adminId = request.getAttribute(ADMIN_ID).toString();

        Admin admin = adminService.getAdminByAdminId(Integer.parseInt(adminId));
        admin.setPassword(""); // 보안

        String Access_jwtToken = tokenService.createToken(ACCESS, adminId); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken(REFRESH, adminId); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(admin);
    }

    /*
     * Login 실패
     * ErrorCode 반환.
     */

    @PostMapping("/loginFail")
    public ResponseEntity<?> loginFail() {
        return ErrorReturn(ApiCode.LOGIN_ERROR);
    }

    /*
     * Access_Token 재발급 API
     * Refresh_Token 인증 진행
     * 성공 : 재발급, 실패 : 오류 코드 반환
     */
    @GetMapping("/tokenReissue")
    public ResponseEntity<?> tokenReissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader(Refresh_JwtProperties.HEADER_STRING)
                    .replace(Refresh_JwtProperties.TOKEN_PREFIX, "");
            String adminId = JWT.require(Algorithm.HMAC512(Refresh_JwtProperties.SECRET)).build().verify(token)
                    .getClaim(ADMIN_ID).asString();

            String Access_jwtToken = tokenService.createToken(ACCESS, adminId); // Access Token 생성
            String Refresh_jwtToken = tokenService.createToken(REFRESH, adminId); // Refresh Token 생성

            response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
            response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

            return SuccessReturn();
        } catch (Exception e) { // Refresh_Toekn 인증 실패 ( 로그인 화면으로 이동 필요 )
            return ErrorReturn(ApiCode.TOKEN_ERROR);
        }
    }

    /*
     * TOKEN 인증 프로세스중 에러 발생
     * ErrorCode 반환.
     */
    @RequestMapping("/tokenError")
    public ResponseEntity<?> tokenError() {
        return ErrorReturn(ApiCode.TOKEN_ERROR);
    }
}
