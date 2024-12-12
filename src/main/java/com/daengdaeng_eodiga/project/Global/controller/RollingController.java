package com.daengdaeng_eodiga.project.Global.controller;


import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.oauth.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController

public class RollingController {
    private final TokenService tokenService;
    @Autowired
    RollingController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    @Value("${server.env}")
    String env;
    @Value("${server.port}")
    String port;
    @Value("${server.serverAddress}")
    String serverAddress;
    @Value("${serverName}")
    String serverName;

    @GetMapping("/hc")
    public ResponseEntity<?> getHC(HttpServletResponse response) {
        // 응답 헤더 설정
        response.addHeader("env", env);
        response.addHeader("port", port);
        response.addHeader("serverAddress", serverAddress);
        response.addHeader("serverName", serverName);

        tokenService.generateTokensAndSetCookies("lixxce5017@gmail.com", OauthProvider.google, response);

        return ResponseEntity.ok(env);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        Map<String,String> response = new HashMap<String,String>();
        return ResponseEntity.ok(env);
    }
}
