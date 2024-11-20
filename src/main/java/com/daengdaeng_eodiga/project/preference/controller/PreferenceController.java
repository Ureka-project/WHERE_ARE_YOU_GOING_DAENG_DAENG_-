package com.daengdaeng_eodiga.project.preference.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    // TODO : header 로부터 유저정보 가져오는 걸로 변경해야 함
    private final int hardcodedUserId = 3;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerPreference(@Validated @RequestBody PreferenceRequestDto preferenceRequestDto) {
        preferenceService.registerPreference(hardcodedUserId, preferenceRequestDto);
        return ResponseEntity.ok(ApiResponse.success("preferences insert succesfully"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePreference(@Validated @RequestBody PreferenceRequestDto preferenceRequestDto) {
        preferenceService.updatePreference(hardcodedUserId, preferenceRequestDto);
        return ResponseEntity.ok(ApiResponse.success("preferences update succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> fetchPreferences(){
        List<PreferenceResponseDto> response =  preferenceService.fetchPreferences(hardcodedUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
