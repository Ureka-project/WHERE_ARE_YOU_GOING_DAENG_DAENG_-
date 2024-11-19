package com.daengdaeng_eodiga.project.oauth.service;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class OauthUserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public String registerUser(SignUpForm userDTO) {
        // 1. 사용자 정보 저장
        User user = new User();
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setCity(userDTO.getCity());
        user.setCityDetail(userDTO.getCityDetail());
        user.setCreatedAt(LocalDateTime.now()); // 생성 시간 설정

        userRepository.save(user); // 유저 저장

        // 2. JWT 토큰 발급
        String accessToken = jwtUtil.createJwt(userDTO.getEmail(), "USER", 60 * 60 * 60L);
        String refreshToken = jwtUtil.createRefreshToken(userDTO.getEmail(), "USER", 24 * 60 * 60 * 1000L);

        // 3. 토큰 반환
        return accessToken;
    }
}
