package com.daengdaeng_eodiga.project.Global.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController

public class RollingController {
    @Value("${server.env}")
    String env;
    @Value("${server.port}")
    String port;
    @Value("${server.serverAddress}")
    String serverAddress;
    @Value("${serverName}")
    @GetMapping("/hc")
    public ResponseEntity<?> getHC() {
        Map<String,String> response = new TreeMap<>();
        response.put("env",env);
        response.put("port",port);
        response.put("serverAddress",serverAddress);
        response.put("serverName",serverAddress);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        Map<String,String> response = new HashMap<String,String>();
        return ResponseEntity.ok(env);
    }
}
