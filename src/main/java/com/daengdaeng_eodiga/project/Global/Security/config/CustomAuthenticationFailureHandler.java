package com.daengdaeng_eodiga.project.Global.Security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = exception.getMessage();
        String email = null;

        // 예외 메시지 출력 (디버깅용)

        // REDIRECT_TO_SIGNUP 메시지에서 이메일 추출
        if (errorMessage != null) {
            // "REDIRECT_TO_SIGNUP:"로 시작하는 경우 이메일 추출
            if (errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
                email = errorMessage.substring("REDIRECT_TO_SIGNUP:".length()); // 이메일을 추출
            }
            // "User not found for email:" 메시지에서 이메일 추출
            else if (errorMessage.startsWith("User not found for email: ")) {
                email = errorMessage.substring("User not found for email: ".length()); // 이메일을 추출
            }
        }

        // 이메일이 없으면 기본값 설정
        if (email == null) {
            email = "unknown@example.com";
        }
        if (errorMessage != null && errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
            response.sendRedirect("/api/signup?email=" + email); // 이메일을 포함하여 /signup 페이지로 리다이렉트
        } else {
            response.sendRedirect("/login?error=unknown"); // 알 수 없는 오류 발생 시 /login 페이지로 리다이렉트
        }
    }

}