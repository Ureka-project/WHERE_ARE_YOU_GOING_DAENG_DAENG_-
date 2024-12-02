package com.daengdaeng_eodiga.project.oauth.service;


import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedDelCookie;
import com.daengdaeng_eodiga.project.oauth.OauthResult;
import com.daengdaeng_eodiga.project.oauth.dto.OauthResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;

    public TokenService(JWTUtil jwtUtil, RedisTokenRepository redisTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
    }

    public HttpServletResponse generateTokensAndSetCookies(String email, HttpServletResponse response) {
        String accessToken = jwtUtil.createJwt(email, jwtUtil.getAccessTokenExpiration());
        String refreshToken = jwtUtil.createRefreshToken(email,jwtUtil.getRefreshTokenExpiration());

        response.addCookie(jwtUtil.createCookie("RefreshToken", refreshToken, jwtUtil.getRefreshTokenExpiration(), response));
        response.addCookie(jwtUtil.createCookie("Authorization", accessToken, jwtUtil.getAccessTokenExpiration(),response));
        redisTokenRepository.saveToken(refreshToken, jwtUtil.getRefreshTokenExpiration(), email);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(jwtUtil.getRefreshTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", accessToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(jwtUtil.getAccessTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        return response;
    }
    public ResponseEntity<ApiResponse<?>> deleteCookie(String email, String RefreshToken, HttpServletResponse response) {
        try {
            // 쿠키 삭제 처리
            Cookie RefreshCookie = jwtUtil.deletRefreshCookie("RefreshToken", null, response);
            Cookie accessCookie = jwtUtil.deletAcessCookie("Authorization", null, response);
            response.addCookie(RefreshCookie);
            response.addCookie(accessCookie);

            // Redis에서 토큰 삭제
            redisTokenRepository.deleteToken(RefreshToken);

            long expiration = jwtUtil.getExpiration(RefreshToken);
            if (expiration > 0) {
                redisTokenRepository.addToBlacklist(RefreshToken, expiration, email);
            }

            // 성공적인 응답
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            // 예외 발생 시 커스텀 예외 던지기
            throw new UserFailedDelCookie();  // 쿠키 삭제 실패 예외를 던짐
        }
    }
}
