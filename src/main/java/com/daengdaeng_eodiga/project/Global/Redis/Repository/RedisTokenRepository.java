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
        try {
            System.out.println("[DEBUG] saveToken 호출됨");
            System.out.println("[DEBUG] 입력값 - email: " + email + ", refreshToken: " + refreshToken + ", expiration: " + expiration);

            // 만료 시간 확인
            if (expiration <= 0) {
                throw new IllegalArgumentException("유효하지 않은 만료 시간: " + expiration);
            }

            // Redis 저장
            redisTemplate.opsForValue().set("refreshToken:" +refreshToken, email, expiration, TimeUnit.MILLISECONDS);
            System.out.println("[DEBUG] Redis에 저장 완료 - Key: refreshToken:" + refreshToken + ", Value: " + email);
            String storedToken =  redisTemplate.opsForValue().get("refreshToken:" + refreshToken);
            System.out.println("Stored refresh token: " + storedToken);

        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            System.err.println("[ERROR] saveToken 처리 중 예외 발생");
            e.printStackTrace();
        }
    }


    public String getToken(String email) {
        return redisTemplate.opsForValue().get("refreshToken:" + email);
    }

    public void deleteToken(String email) {
        redisTemplate.delete("refreshToken:" + email);
    }

    public void addToBlacklist(String token, long expiration, String email) {
        try {

                redisTemplate.opsForValue().set("blacklist:" + token, email, expiration, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Redis 블랙리스트 추가 중 오류 발생: " + e.getMessage());
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }

}
