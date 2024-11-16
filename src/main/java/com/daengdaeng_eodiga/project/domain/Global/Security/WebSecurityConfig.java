/*package com.daengdaeng_eodiga.domain.Global.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP 보안 설정
        http

                .authorizeRequests() // 권한 설정 메서드
                .requestMatchers("/", "/home").permitAll() // 특정 URL에 대한 접근 허용
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                .and()
                .formLogin(withDefaults()) // 커스텀 로그인 페이지 (기본값 사용)
                .and()
                .logout(withDefaults()); // 로그아웃 설정 (기본값 사용)



        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 데이터베이스에서 사용자 정보 가져오기 로직 (예시로 인메모리 사용자 설정)
            if ("user".equals(username)) {
                return User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .build();
            } else if ("admin".equals(username)) {
                return User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
*/
