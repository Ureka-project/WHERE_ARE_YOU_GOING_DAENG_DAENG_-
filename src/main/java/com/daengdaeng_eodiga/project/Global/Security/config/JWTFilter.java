package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;

    public JWTFilter(JWTUtil jwtUtil, RedisTokenRepository redisTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    accessToken = cookie.getValue();
                }
                else if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                }

            }

            // 액세스 토큰이 없으면 바로 필터 체인 실행
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            token = accessToken;
            // 블랙리스트 확인
            if (redisTokenRepository.isBlacklisted(refreshToken)&&refreshToken==null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted.");
                return;
            }

            // 토큰 검증 및 재발급
            if (jwtUtil.isExpired(accessToken)&&refreshToken==null) {

                if (!redisTokenRepository.isBlacklisted(refreshToken) && !jwtUtil.isExpired(refreshToken))  {
                    String newAccessToken = jwtUtil.createJwt(jwtUtil.getUsername(token), jwtUtil.getRole(refreshToken), jwtUtil.getEmail(refreshToken), 60 * 60 * 60L);
                    response.addCookie(JWTUtil.createCookie("Authorization", newAccessToken));
                    token = newAccessToken;
                }

            }
        }

        // 액세스 토큰이 유효하면 사용자 인증 처리
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        String email = jwtUtil.getEmail(token);

        UserOauthDto userDTO = new UserOauthDto();
        userDTO.setName(username);
        userDTO.setRole(role);
        userDTO.setEmail(email);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


}