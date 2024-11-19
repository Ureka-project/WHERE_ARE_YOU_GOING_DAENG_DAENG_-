package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

        public CustomSuccessHandler(JWTUtil jwtUtil,UserRepository userRepository) {
            this.jwtUtil = jwtUtil;
            this.userRepository = userRepository;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // OAuth2User
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
            String username = customUserDetails.getName();

            // 사용자 권한 가져오기
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            // 사용자 정보 가져오기
            User user = userRepository.findByEmail(username);  // 이메일을 기준으로 사용자 조회

            // 액세스 토큰 생성
            String accessToken = jwtUtil.createJwt(username, role, 60 * 60 * 60L);
            String newRefreshToken = jwtUtil.createRefreshToken(username, role, 24 * 60 * 60 * 1000L); // 24시간 유효기간


            // 액세스 토큰 쿠키에 추가
            response.addCookie(JWTUtil.createCookie("Authorization", accessToken));
            response.addCookie(JWTUtil.createRefreshCookie("RefreshToken", newRefreshToken));

            // 인증 성공 후 리다이렉트
            //바로 회원정보로가서 회원정보 받아서 기입
            response.sendRedirect("/api/loginSuccess");
        }


    }

