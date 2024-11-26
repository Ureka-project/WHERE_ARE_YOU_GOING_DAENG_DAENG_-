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
        if (errorMessage != null) {
            if (errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
                email = errorMessage.substring("REDIRECT_TO_SIGNUP:".length());
            }
            else if (errorMessage.startsWith("User not found for email: ")) {
                email = errorMessage.substring("User not found for email: ".length());
            }
        }

        if (email == null) {
            email = "unknown@example.com";
        }
        if (errorMessage != null && errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
            response.sendRedirect("/api/v1/signup?email=" + email);
        } else {
            response.sendRedirect("/login?error=unknown");
        }
    }

}