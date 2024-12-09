package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.oauth.controller.OuathController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final OuathController ouathController;

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
                        //TODO : OauthProvider 소문자,대문자 통일
                        try{
                            provider = OauthProvider.valueOf(providerValue.toLowerCase());
                        }catch (Exception e){
                            provider = OauthProvider.google;
                        }
                        ouathController.showSignUpForm(email, provider.toString(), response);

                    }
                }
            } else if (errorMessage.startsWith("DELETED_USER:")) {
                request.setAttribute("exception", new DuplicateUserException());
            }
        }else{
            response.sendRedirect("/login?error=unknown");
        }

    }

}