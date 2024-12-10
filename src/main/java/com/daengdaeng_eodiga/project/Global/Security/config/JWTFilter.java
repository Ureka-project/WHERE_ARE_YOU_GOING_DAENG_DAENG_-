package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.enums.Jwtexception;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.user.entity.User;
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

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;
    private final UserService userService;

    private final Boolean testMode;
    public JWTFilter(JWTUtil jwtUtil, RedisTokenRepository redisTokenRepository,UserService userService, Boolean testMode) {
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
        this.userService = userService;
        this.testMode = testMode;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal - JWTFilter : " + request.getRequestURI()+ " "+request.getMethod()+" cookies : "+request.getCookies());
        Cookie[] cookies = request.getCookies();
        String email = "user1@example.com";
        OauthProvider provider = OauthProvider.google;
        if(!testMode){
            if((!request.getRequestURI().startsWith("/api/v1/") && !request.getRequestURI().startsWith("/api/v2/")) || request.getCookies() == null){
                log.info("cookies is null or requestUri is not /api/v1/or/api/v2/");
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

            if(accessToken==null || refreshToken==null){
                log.info("accessToken or refreshToken is null");
                filterChain.doFilter(request, response);
                return;
            }

            log.info("accessToken : "+accessToken);
            log.info("refreshToken : "+refreshToken);

            Jwtexception accessTokenValid = jwtUtil.isJwtValid(accessToken);
            if (accessTokenValid != Jwtexception.normal&&accessTokenValid != Jwtexception.expired) {
                log.info("accessToken is not valid");
                filterChain.doFilter(request, response);
                return;
            } else if (accessTokenValid == Jwtexception.expired) {
                Jwtexception refreshTokenValid = jwtUtil.isJwtValid(refreshToken);

                if (refreshTokenValid != Jwtexception.normal) {
                    if (refreshTokenValid == Jwtexception.expired) {
                        log.info("refreshToken is expired");
                    } else {
                        log.info("refreshToken is not valid");
                    }
                    filterChain.doFilter(request, response);
                    return;
                } else if (!redisTokenRepository.isBlacklisted(refreshToken)) {
                    log.info("refreshToken is not expired, so new accessToken is created");
                    accessToken = jwtUtil.createJwt(jwtUtil.getEmail(refreshToken),jwtUtil.getProvider(refreshToken).toString(), jwtUtil.getAccessTokenExpiration());
                    response.addHeader("Set-Cookie", jwtUtil.createCookie("Authorization", accessToken,
                            jwtUtil.getAccessTokenExpiration()).toString());
                } else {
                    log.info("logout된 토큰이다.");
                    filterChain.doFilter(request, response);
                    return;
                }
            }


            email = jwtUtil.getEmail(accessToken);
            provider = jwtUtil.getProvider(accessToken);
        }


        log.info("filter email : "+email + " provider : "+provider);
        UserOauthDto userDTO = new UserOauthDto();
        User user= userService.findUserByemailAndProvider(email,provider);
        userDTO.setUserid(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setProvider(provider);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);

    }


}