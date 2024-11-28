package com.daengdaeng_eodiga.project.oauth.service;


import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.oauth.OauthResult;
import com.daengdaeng_eodiga.project.oauth.dto.OauthResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    public ResponseEntity<ApiResponse<?>> generateTokensAndSetCookies(String email, HttpServletResponse response) {

        String accessToken = jwtUtil.createJwt(email, 60 * 60 * 60L);
        String refreshToken = jwtUtil.createRefreshToken(email, 24 * 60 * 60 * 1000L);

        Cookie accessTokenCookie = jwtUtil.createCookie("Authorization", accessToken,60 * 60 * 60,response);
        Cookie refreshTokenCookie = jwtUtil.createCookie("RefreshToken", refreshToken,24 * 60 * 60 * 1000,response);

        redisTokenRepository.saveToken(refreshToken, 24 * 60 * 60 * 1000L, email);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        OauthResponse oauthResponse = new OauthResponse(null,OauthResult.LOGIN_SUCCESS);
        return ResponseEntity.ok(ApiResponse.success(oauthResponse));
    }

        public ResponseEntity<ApiResponse<?>> deleteCookie(String email,String RefreshToken,HttpServletResponse response) {

            Cookie RefreshCookie = jwtUtil.deletRefreshCookie("RefreshToken", null,response);
            Cookie accessCookie = jwtUtil.deletAcessCookie("Authorization", null,response);
            response.addCookie(RefreshCookie);
            response.addCookie(accessCookie);
            redisTokenRepository.deleteToken(RefreshToken);
            long expiration = jwtUtil.getExpiration(RefreshToken);
            if (expiration > 0) {
                redisTokenRepository.addToBlacklist(RefreshToken, expiration, email);
            }
            return ResponseEntity.ok(ApiResponse.success(response));
        }
}
