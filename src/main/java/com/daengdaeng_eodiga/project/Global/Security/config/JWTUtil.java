package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.enums.Jwtexception;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
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
    @Getter
    @Value("${jwt.token-expiration.access}")
    private int accessTokenExpiration;

    @Getter
    @Value("${jwt.token-expiration.refresh}")
    private int refreshTokenExpiration;

    public String getEmail(String token) {
        String email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
        log.info("jwt - getEmail : " + email);
        return email;
    }

    public Jwtexception isJwtValid(String token) {
        try {
             Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
             return Jwtexception.normal;
        } catch (ExpiredJwtException e) {
            return Jwtexception.expired;
        }
        catch (JwtException e) {
            return  Jwtexception.mismatch;
        }
    }

    public String createJwt(String email, int expiredMs) {
        log.info("jwt - createJwt email: " + email);
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshToken(String email, int expiredMs) {
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String key, String value, int expiredMs, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredMs);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setDomain("daengdaeng-where.link");

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