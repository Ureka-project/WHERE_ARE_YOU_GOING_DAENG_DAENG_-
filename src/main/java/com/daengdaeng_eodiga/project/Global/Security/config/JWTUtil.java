package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.enums.Jwtexception;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final String text_key;
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey =  Keys.hmacShaKeyFor(secret.getBytes());
        this.text_key=secret;
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

    public OauthProvider getProvider (String token) {
        String provider = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("provider", String.class);
        log.info("jwt - getProvider : " + provider);
        return OauthProvider.valueOf(provider);
    }
    public long getExpiration(String token) {
        try {
            Date expiration =Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token).getPayload().getExpiration();

            LocalDateTime targetTime = expiration.toInstant()
                    .atZone(ZoneId.of("CST6CDT"))
                    .toLocalDateTime();

            LocalDateTime now = LocalDateTime.now(ZoneId.of("CST6CDT"));

            return Duration.between(now, targetTime).getSeconds();
        } catch (Exception e) {
            return 0;
        }
    }

    public Jwtexception isJwtValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(text_key.getBytes());

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Jwtexception.normal;

        } catch (ExpiredJwtException e) {
            return Jwtexception.expired;
        } catch (MalformedJwtException e) {
            return Jwtexception.mismatch;
        }
        catch (UnsupportedJwtException e) {
            return Jwtexception.unsupportedJwt;
        } catch (IllegalArgumentException e) {
            return Jwtexception.invalidArgument;
        } catch (JwtException e) {
            return Jwtexception.otherError;
        }
    }


    public String createJwt(String email, String provider, int expiredMs) {
        log.info("jwt - createJwt email: " + email);
        long now = new Date().getTime();
        return Jwts.builder()
                .claim("email", email)
                .claim("provider", provider)
                .issuedAt(new Date(now))
                .expiration(new Date(now+expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshToken(String email, String provider, int expiredMs) {
        long now = new Date().getTime();
        return Jwts.builder()
                .claim("email", email)
                .claim("provider", provider)
                .issuedAt(new Date(now))
                .expiration(new Date(now+expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public ResponseCookie createCookie(String key, String value, int expiredMs) {
        return ResponseCookie.from(key, value)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .maxAge(expiredMs)
                .domain(".daengdaeng-where.link")
                .build();
    }
}