package com.daengdaeng_eodiga.project.Global.Security.config;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    public  Cookie createCookie(String key, String value, int expiredMs, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredMs);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String cookieWithSameSite = String.format(
                "%s=%s; Max-Age=%d; Path=%s; SameSite=None",
                key, value, expiredMs, "/"
        );
        response.addHeader("Set-Cookie", cookieWithSameSite);
        return cookie;
    }
    public  Cookie deletAcessCookie(String key, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(false); // 로컬 테스트 환경에서는 Secure 비활성화
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String cookieWithSameSite = String.format(
                "%s=%s; Max-Age=%d; Path=%s; SameSite=None",
                key, value, 0, "/"
        );
        response.addHeader("Set-Cookie", cookieWithSameSite);
        return cookie;
    }
    public Cookie deletRefreshCookie(String key, String value,HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(false);
        String cookieWithSameSite = String.format(
                "%s=%s; Max-Age=%d; Path=%s; SameSite=None",
                key, null, 0, "/"
        );
        response.addHeader("Set-Cookie", cookieWithSameSite);
        return cookie;
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