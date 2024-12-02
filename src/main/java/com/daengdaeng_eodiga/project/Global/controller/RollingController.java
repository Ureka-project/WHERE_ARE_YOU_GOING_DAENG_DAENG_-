package com.daengdaeng_eodiga.project.Global.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RollingController {

    String env;
    @GetMapping("/hc")
    public ResponseEntity<?> getHC() {
        Map<String,String> response = new HashMap<String,String>();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        Map<String,String> response = new HashMap<String,String>();
        return ResponseEntity.ok(response);
    }
}
