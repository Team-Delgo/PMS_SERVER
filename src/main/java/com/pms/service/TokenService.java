package com.pms.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pms.comm.security.jwt.Access_JwtProperties;
import com.pms.comm.security.jwt.Refresh_JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    // Create Token
    public String createToken(String tokenType, String adminId) {
        if (tokenType.equals("Access")) // Access Token
            return JWT.create()
                    .withSubject(adminId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + Access_JwtProperties.EXPIRATION_TIME))
                    .withClaim("adminId", adminId)// getUsername() == getEmail()
                    .sign(Algorithm.HMAC512(Access_JwtProperties.SECRET));
        else // Refresh Token
            return JWT.create()
                    .withSubject(adminId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + Refresh_JwtProperties.EXPIRATION_TIME))
                    .withClaim("adminId", adminId)// getUsername() == getEmail()
                    .sign(Algorithm.HMAC512(Refresh_JwtProperties.SECRET));
    }
}
