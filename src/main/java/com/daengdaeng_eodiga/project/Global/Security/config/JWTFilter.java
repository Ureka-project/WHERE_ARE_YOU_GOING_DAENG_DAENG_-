package com.daengdaeng_eodiga.project.Global.Security.config;

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

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
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

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
                if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 액세스 토큰이 없으면 바로 필터 체인 실행
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        // 액세스 토큰이 만료되었는지 확인
        if (jwtUtil.isExpired(token)) {
            // 리프레시 토큰 확인
            if (refreshToken != null && !jwtUtil.isRefreshTokenExpired(refreshToken)) {
                // 리프레시 토큰이 유효한 경우 새 액세스 토큰 발급
                String username = jwtUtil.getUsername(refreshToken);
                String role = jwtUtil.getRole(refreshToken);
                String newAccessToken = jwtUtil.createJwt(username, role, 60 * 60 * 60L); // 1시간 유효기간

                // 새 토큰을 쿠키로 응답에 추가
                Cookie accessTokenCookie = new Cookie("Authorization", newAccessToken);
                accessTokenCookie.setHttpOnly(true);
                accessTokenCookie.setPath("/");
                accessTokenCookie.setMaxAge(60 * 60); // 1시간

                response.addCookie(accessTokenCookie);
                token = newAccessToken; // 새로 발급받은 액세스 토큰을 사용
            } else {
                // 리프레시 토큰이 만료되었거나 없는 경우
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect("/login"); // 로그인 페이지 경로로 변경
                return;
            }
        }

        // 액세스 토큰이 유효하면 사용자 인증 처리
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserOauthDto userDTO = new UserOauthDto();
        userDTO.setName(username);
        userDTO.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


}