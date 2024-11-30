package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

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
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
            String email=customUserDetails.getEmail();


            Optional<User> Quser = userRepository.findByEmail(email);
            User user = new User();
            if (Quser.isPresent()) {
                 user = Quser.get();
            }
            String accessToken = jwtUtil.createJwt(email, 60 * 60 * 60L);
            String refreshToken = jwtUtil.createRefreshToken(email,24 * 60 * 60 * 1000L);
            ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(60 * 60 * 60)
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());
            ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", accessToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(60 * 60 * 60)
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", accessTokenCookie.toString());
            redisTokenRepository.saveToken(refreshToken, 24 * 60 * 60 * 1000L, user.getEmail());
            response.sendRedirect("https://api.daengdaeng-where.link/");
        }


    }

