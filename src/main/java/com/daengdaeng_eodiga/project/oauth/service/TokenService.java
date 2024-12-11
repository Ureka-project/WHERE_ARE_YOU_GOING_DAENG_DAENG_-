package com.daengdaeng_eodiga.project.oauth.service;


import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedDelCookie;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.oauth.OauthResult;
import com.daengdaeng_eodiga.project.oauth.dto.OauthResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;

    public TokenService(JWTUtil jwtUtil, RedisTokenRepository redisTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
    }

    public void generateTokensAndSetCookies(String email, OauthProvider provider, HttpServletResponse response) {
        String accessToken = jwtUtil.createJwt(email, provider.toString(), jwtUtil.getAccessTokenExpiration());
        String refreshToken = jwtUtil.createRefreshToken(email, provider.toString(), jwtUtil.getRefreshTokenExpiration());

        redisTokenRepository.saveToken(refreshToken, jwtUtil.getRefreshTokenExpiration(), email);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .maxAge(jwtUtil.getRefreshTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", accessToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .maxAge(jwtUtil.getAccessTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie isLogin = ResponseCookie.from("loginSuccess", String.valueOf(true))
            .path("/")
            .sameSite("Lax")
            .httpOnly(false)
            .secure(true)
            .maxAge(jwtUtil.getAccessTokenExpiration())
            .domain(".daengdaeng-where.link")
            .build();
        response.addHeader("Set-Cookie", isLogin.toString());

    }
    public void deleteCookie(String email, HttpServletResponse response,Cookie Refresh) {
        try {
            redisTokenRepository.deleteToken(email);
            ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken")
                    .path("/")
                    .sameSite("Lax")
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(0)
                    .domain(".daengdaeng-where.link")
                    .build();
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());
            ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization")
                    .path("/")
                    .sameSite("Lax")
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(0)
                    .domain(".daengdaeng-where.link")
                    .build();
            response.addHeader("Set-Cookie", accessTokenCookie.toString());

            ResponseCookie isLogin = ResponseCookie.from("loginSuccess", String.valueOf(false))
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(0)
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", isLogin.toString());

            long expiration = jwtUtil.getExpiration(Refresh.getValue());
            if (expiration > 0) {
                redisTokenRepository.addToBlacklist(Refresh.getValue(), expiration, email);
            }

        } catch (Exception e) {

            throw new UserFailedDelCookie();
        }
    }
}
