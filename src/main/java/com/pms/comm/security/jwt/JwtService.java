package com.pms.comm.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pms.comm.exception.ApiCode;
import com.pms.comm.exception.BaseException;
import com.pms.comm.security.jwt.config.AccessTokenProperties;
import com.pms.comm.security.jwt.config.RefreshTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * JWT 토큰 서비스
 * <p>
 * jwt토큰 생성, 얻기, 인증, 검증, 토큰에서 adminId 추출
 */
@Slf4j
@Service
public class JwtService {
    /**
     * JWT 생성
     *
     * @param adminId, role
     * @return String
     **/
    // Create Token
    public JwtToken createToken(int adminId) {
        return new JwtToken(
                JWT.create() // Access Token
                        .withSubject(String.valueOf(adminId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + AccessTokenProperties.EXPIRATION_TIME))
                        .withClaim("adminId", adminId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(AccessTokenProperties.SECRET)),
                JWT.create() // Refresh_Token
                        .withSubject(String.valueOf(adminId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + RefreshTokenProperties.EXPIRATION_TIME))
                        .withClaim("adminId", adminId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(RefreshTokenProperties.SECRET))
        );
    }

    /**
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     *
     * @return String
     **/
    public String getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(AccessTokenProperties.HEADER_STRING).replace(AccessTokenProperties.TOKEN_PREFIX, "");
    }

    /**
     * Header에서 X-REFRESH-TOKEN 으로 JWT 추출
     *
     * @return String
     **/
    public String getRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(RefreshTokenProperties.HEADER_STRING).replace(RefreshTokenProperties.TOKEN_PREFIX, "");
    }

    /**
     * JWT에서 adminId 추출
     *
     * @return int
     */
    public Integer getAdminIdByAccessToken() throws BaseException {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.length() == 0)
            throw new BaseException(ApiCode.TOKEN_ERROR);

        return Integer.parseInt(String.valueOf(JWT.require(Algorithm.HMAC512(AccessTokenProperties.SECRET)).build().verify(accessToken).getClaim("adminId")));
    }

    public Integer getAdminIdByRefreshToken() throws BaseException {
        String refreshToken = getRefreshToken();
        if (refreshToken == null || refreshToken.length() == 0) throw new BaseException(ApiCode.TOKEN_ERROR);

        return Integer.parseInt(String.valueOf(JWT.require(Algorithm.HMAC512(RefreshTokenProperties.SECRET)).build().verify(refreshToken).getClaim("adminId")));
    }
}

