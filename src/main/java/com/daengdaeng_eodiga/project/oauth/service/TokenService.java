package com.daengdaeng_eodiga.project.oauth.service;


import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
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

        String accessToken = jwtUtil.createJwt(email, 60 * 60 * 60L); // 60*60*60L은 1시간
        String refreshToken = jwtUtil.createRefreshToken(email, 24 * 60 * 60 * 1000L); // 1일

        Cookie accessTokenCookie = jwtUtil.createCookie("Authorization", accessToken,60 * 60 * 60);
        Cookie refreshTokenCookie = jwtUtil.createCookie("RefreshToken", refreshToken,24 * 60 * 60 * 1000);

        redisTokenRepository.saveToken(refreshToken, 24 * 60 * 60 * 1000L, email);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

        public ResponseEntity<ApiResponse<?>> deleteCookie(String email,String RefreshToken,HttpServletResponse response) {

            Cookie RefreshCookie = jwtUtil.deletRefreshCookie("RefreshToken", null);
            Cookie accessCookie = jwtUtil.deletAcessCookie("Authorization", null);
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
