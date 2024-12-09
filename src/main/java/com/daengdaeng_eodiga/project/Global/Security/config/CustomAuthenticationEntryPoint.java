package com.daengdaeng_eodiga.project.Global.Security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import com.daengdaeng_eodiga.project.Global.dto.ApiErrorResponse;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Throwable exception = (Throwable) request.getAttribute("exception");

        if (exception != null) {
            if (exception instanceof DuplicateUserException) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.setContentType("application/json;charset=UTF-8");
                log.error(HttpStatus.CONFLICT.name()+ " : " + exception.getMessage());
                ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.CONFLICT.name(), "탈퇴된 유저입니다.");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
            }
        }else {
            log.error(HttpStatus.UNAUTHORIZED.name()+ " : " + authException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            ApiErrorResponse errorResponse = ApiErrorResponse.error(HttpStatus.UNAUTHORIZED.name(), "인증되지 않은 요청입니다.");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        }

    }
}