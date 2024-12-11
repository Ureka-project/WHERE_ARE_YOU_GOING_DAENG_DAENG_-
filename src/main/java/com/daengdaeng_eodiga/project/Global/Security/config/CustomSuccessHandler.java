package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
@Slf4j
@Component
    public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JWTUtil jwtUtil;
        private final UserRepository userRepository; // UserRepository 추가
        private final RedisTokenRepository redisTokenRepository;

        @Value("${frontend.url}")
        private String frontUrl;

        public CustomSuccessHandler(JWTUtil jwtUtil,UserRepository userRepository, RedisTokenRepository redisTokenRepository) {
            this.jwtUtil = jwtUtil;
            this.userRepository = userRepository;
            this.redisTokenRepository = redisTokenRepository;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
            String email=customUserDetails.getEmail();
            OauthProvider provider=customUserDetails.getUserDTO().getProvider();

            log.info("onAuthenticationSuccess - CustomSuccessHandler : " + email);

            Optional<User> Quser = userRepository.findByEmailAndOauthProviderAndDeletedAtIsNull(email,provider);
            User user = new User();
            if (Quser.isPresent()) {
                 user = Quser.get();
            }
            String accessToken = jwtUtil.createJwt(email, provider.toString(), jwtUtil.getAccessTokenExpiration());
            String refreshToken = jwtUtil.createRefreshToken(email,provider.toString(), jwtUtil.getRefreshTokenExpiration());
            ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .maxAge(jwtUtil.getRefreshTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());
            ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", accessToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .maxAge(jwtUtil.getAccessTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", accessTokenCookie.toString());

            ResponseCookie isLogin = ResponseCookie.from("loginSuccess", String.valueOf(true))
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(true)
                .maxAge(jwtUtil.getAccessTokenExpiration())
                .domain(".daengdaeng-where.link")
                .build();
            response.addHeader("Set-Cookie", isLogin.toString());
            redisTokenRepository.saveToken(refreshToken, jwtUtil.getRefreshTokenExpiration(), user.getEmail());
            response.sendRedirect(frontUrl);
        }


    }

