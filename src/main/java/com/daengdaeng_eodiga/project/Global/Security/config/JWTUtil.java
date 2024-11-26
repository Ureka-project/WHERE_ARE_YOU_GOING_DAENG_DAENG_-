package com.daengdaeng_eodiga.project.Global.Security.config;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getEmail(String token) {
        String email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
        log.info("jwt - getEmail : " + email);
        return email;
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
            log.info("jwt - isExpired : " + e.getMessage());
            return true;
        }
    }

    public String createJwt(String email, Long expiredMs) {
        log.info("jwt - createJwt email: " + email);
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshToken(String email, Long expiredMs) {
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public  Cookie createCookie(String key, String value,int expiredMs) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredMs);
        cookie.setPath("/");
        //TODO :  서버 테스트 동안만 주석

        // cookie.setSecure(true);
        // cookie.setHttpOnly(true);
        return cookie;
    }
    public  Cookie deletAcessCookie(String key, String value) {
        Cookie accessTokenCookie = new Cookie("Authorization", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        return accessTokenCookie;
    }
    public Cookie deletRefreshCookie(String key, String value) {
        Cookie refreshTokenCookie = new Cookie("RefreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        return refreshTokenCookie;
    }



    public long getExpiration(String token) {
        try {
            Date expiration =Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token).getBody().getExpiration();

            LocalDateTime targetTime = expiration.toInstant()
                    .atZone(ZoneId.of("CST6CDT"))
                    .toLocalDateTime();

            LocalDateTime now = LocalDateTime.now(ZoneId.of("CST6CDT"));

            long secondsBetween = Duration.between(now, targetTime).getSeconds();

            return secondsBetween;
        } catch (Exception e) {
            return 0;
        }
    }
}