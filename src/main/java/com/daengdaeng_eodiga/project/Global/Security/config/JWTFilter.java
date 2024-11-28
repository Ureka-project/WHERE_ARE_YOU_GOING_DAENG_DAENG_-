package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;
    private final UserService userService;
    public JWTFilter(JWTUtil jwtUtil, RedisTokenRepository redisTokenRepository,UserService userService) {
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal - JWTFilter : " + request.getRequestURI()+ " "+request.getMethod()+" cookies : "+request.getCookies());
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }


        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    accessToken = cookie.getValue();
                } else if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                }

            }

        }
        //accessToken =request.getHeader("Authorization");
        //refreshToken=request.getHeader("RefreshToken");
        log.info("accessToken : "+accessToken);
        log.info("refreshToken : "+refreshToken);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        token = accessToken;
        if (!redisTokenRepository.isBlacklisted(refreshToken) && !jwtUtil.isExpired(refreshToken)&&jwtUtil.isExpired(token))  {
            String newAccessToken = jwtUtil.createJwt(jwtUtil.getEmail(refreshToken), 60 * 60 * 60L);
            response.addCookie(jwtUtil.createCookie("Authorization", newAccessToken,60*60*60,response));
            token = newAccessToken;
            response.addHeader("Authorization",token);
            log.info("refreshToken is not blacklisted and not expired , so new accessToken is created");
        }
        else if (redisTokenRepository.isBlacklisted(refreshToken)&&refreshToken==null) {
            log.info("refreshToken is null and blacklisted");
            filterChain.doFilter(request, response);
        }
     

        String email = jwtUtil.getEmail(token);
        UserOauthDto userDTO = new UserOauthDto();
        User user= userService.findUserId(email);

        userDTO.setUserid(user.getUserId());
        userDTO.setEmail(user.getEmail());
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


}