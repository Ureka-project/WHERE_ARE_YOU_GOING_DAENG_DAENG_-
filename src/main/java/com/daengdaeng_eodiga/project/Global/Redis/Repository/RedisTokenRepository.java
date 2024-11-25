package com.daengdaeng_eodiga.project.Global.Redis.Repository;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenRepository {
    public final RedisTemplate<String, String> redisTemplate;
    public final  JWTUtil jwtUtil;
    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate,JWTUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    public void saveToken(String refreshToken, long expiration,String email) {
            if (expiration <= 0) {
                throw new IllegalArgumentException("유효하지 않은 만료 시간: " + expiration);
            }
            redisTemplate.opsForValue().set("refreshToken:" +refreshToken, email, expiration, TimeUnit.MILLISECONDS);
            String storedToken =  redisTemplate.opsForValue().get("refreshToken:" + refreshToken);
    }


    public String getToken(String email) {
        return redisTemplate.opsForValue().get("refreshToken:" + email);
    }

    public void deleteToken(String email) {
        redisTemplate.delete("refreshToken:" + email);
    }

    public void addToBlacklist(String token, long expiration, String email) {
        redisTemplate.opsForValue().set("blacklist:" + token, email, expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }

}