package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;
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

        OauthProvider provider = null;
        if (errorMessage != null) {
            if (errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
                String[] parts = errorMessage.substring("REDIRECT_TO_SIGNUP:".length()).trim().split(", ");
                for (String part : parts) {
                    if (part.startsWith("email=")) {
                        email = part.substring("email=".length());
                    } else if (part.startsWith("provider=")) {
                        String providerValue = part.substring("provider=".length());
                        provider = OauthProvider.valueOf(providerValue.toUpperCase());
                    }
                }
            }
        }
        if (email == null) {
            email = "unknown@example.com";
        }
        if (errorMessage != null && errorMessage.startsWith("REDIRECT_TO_SIGNUP:")) {
            response.sendRedirect("/api/v1/signup?email=" + email + "&provider=" + provider);
        } else {
            response.sendRedirect("/login?error=unknown");
        }
    }

}