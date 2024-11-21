package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
    public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JWTUtil jwtUtil;
        private final UserRepository userRepository; // UserRepository 추가
        private final RedisTokenRepository redisTokenRepository;
        public CustomSuccessHandler(JWTUtil jwtUtil,UserRepository userRepository, RedisTokenRepository redisTokenRepository) {
            this.jwtUtil = jwtUtil;
            this.userRepository = userRepository;
            this.redisTokenRepository = redisTokenRepository;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // OAuth2User
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
            String username = customUserDetails.getName();
            String email=customUserDetails.getEmail();

            // 사용자 권한 가져오기
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();



            User user = userRepository.findByEmail(email);  // 이메일을 기준으로 사용자 조회

            String accessToken = jwtUtil.createJwt(username,role,email, 60 * 60 * 60L);
            String newRefreshToken = jwtUtil.createRefreshToken(username,role,email,24 * 60 * 60 * 1000L); // 24시간 유효기간

            response.addCookie(JWTUtil.createCookie("RefreshToken", newRefreshToken));
            response.addCookie(JWTUtil.createCookie("Authorization", accessToken));
            redisTokenRepository.saveToken(newRefreshToken, 24 * 60 * 60 * 1000L, user.getEmail());
            response.sendRedirect("/api/loginSuccess");
        }


    }

