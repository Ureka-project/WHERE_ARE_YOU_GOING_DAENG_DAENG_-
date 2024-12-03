package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.enums.Jwtexception;
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
        Cookie[] cookies = request.getCookies();

        if((!request.getRequestURI().startsWith("/api/v1/")) || request.getCookies() ==null){
            log.info("cookies is null or requestUri is not /api/v1/");
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = null ;
        String refreshToken = null ;

        log.info("cookies size : "+cookies.length);
        for (Cookie cookie : cookies) {
            log.info("cookie name : "+cookie.getName());
            log.info("cookie value : "+cookie.getValue());
            if (cookie.getName().equals("Authorization")) {
                accessToken = cookie.getValue();
            } else if (cookie.getName().equals("RefreshToken")) {
                refreshToken = cookie.getValue();
            }

        }



        String email = "13wjdgkbbb@gmial.com";
        log.info("filter email : "+email);
        log.info("filter accessToken : "+accessToken);
        UserOauthDto userDTO = new UserOauthDto();
        User user= userService.findUserByemail(email);
        userDTO.setUserid(user.getUserId());
        userDTO.setEmail(user.getEmail());
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);

    }


}