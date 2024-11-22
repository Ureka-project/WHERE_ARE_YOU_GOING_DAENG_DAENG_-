package com.daengdaeng_eodiga.project.oauth.service;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthUserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public void registerUser(SignUpForm userDTO) {
        if (userRepository.existsByNickname(userDTO.getNickname())) {
            System.out.println("이미 사용 중인 닉네임");
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        User user = new User();
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setCity(userDTO.getCity());
        user.setCityDetail(userDTO.getCityDetail());
        user.setCreatedAt(LocalDateTime.now()); // 생성 시간 설정

        userRepository.save(user); // 유저 저장
    }
    public boolean deleteUserByName(String email) {
        // 사용자 이름을 기반으로 삭제 로직 수행
        System.out.println(email);
        User user =userRepository.findByEmail(email);
        if (user!=null) {
            userRepository. deleteById(user.getUserId());
            return true;
        }
        return false;
    }
}
