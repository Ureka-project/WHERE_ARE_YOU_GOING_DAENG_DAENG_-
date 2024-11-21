package com.daengdaeng_eodiga.project.Global.Security.config;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;


@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Boolean isExpired(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 만료된 토큰으로 예외가 발생한 경우에도 true 반환
        }
    }

    public String createJwt(String username ,String role,String email, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshToken(String username, String role,String email, Long expiredMs) {
        return Jwts.builder()
                .claim("username", username)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Boolean isRefreshTokenExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        }

    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60); // 쿠키 유효 시간 1시간
        cookie.setPath("/"); // 전체 도메인에서 접근 가능
        //cookie.setHttpOnly(true); // 클라이언트에서 접근 불가
        return cookie;
    }
    public static Cookie createRefreshCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);  // 24시간 (24시간 * 60분 * 60초)
        cookie.setPath("/");  // 전체 도메인에서 접근 가능
        //cookie.setHttpOnly(true);  // 클라이언트에서 접근 불가
       // cookie.setSecure(true);  // HTTPS에서만 전송 (필요한 경우 설정)
        return cookie;
    }
    public static Cookie deletAcessCookie(String key, String value) {
        Cookie accessTokenCookie = new Cookie("Authorization", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // 쿠키 삭제
        System.out.println("Cookie 삭제 호출");
        return accessTokenCookie;
    }
    public static Cookie deletRefreshCookie(String key, String value) {
        Cookie refreshTokenCookie = new Cookie("RefreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 쿠키 삭제
        return refreshTokenCookie;
    }



    public long getExpiration(String token) {
        try {
            Date expiration =Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token).getBody().getExpiration();

            // Date -> LocalDateTime 변환
            LocalDateTime targetTime = expiration.toInstant()
                    .atZone(ZoneId.of("CST6CDT")) // CST 시간대 지정
                    .toLocalDateTime();

            // 현재 시간
            LocalDateTime now = LocalDateTime.now(ZoneId.of("CST6CDT"));

            // 시간 차이 계산
            long secondsBetween = Duration.between(now, targetTime).getSeconds();

            // 결과 출력
            System.out.println("현재 시간부터 목표 시간까지 남은 초: " + secondsBetween);
            return secondsBetween;  // 남은 만료 시간 반환
        } catch (Exception e) {
            // 예외 발생 시 0 반환 (유효하지 않은 토큰)
            return 0;
        }
    }
    }