package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;
    private final UserService userService;
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil,
    RedisTokenRepository redisTokenRepository,UserService userService) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.redisTokenRepository = redisTokenRepository;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());
        http
           .addFilterBefore(new JWTFilter(jwtUtil,redisTokenRepository,userService), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                        .failureHandler(new CustomAuthenticationFailureHandler())
                );


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login", "/api/v1/signup", "/static/**", "/css/**", "/js/**", "/images/**","signupPage.html").permitAll()
                        .anyRequest().authenticated())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .permitAll());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}