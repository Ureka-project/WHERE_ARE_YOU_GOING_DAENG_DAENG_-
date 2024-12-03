package com.daengdaeng_eodiga.project.preference.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.service.PreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerPreference(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody PreferenceRequestDto preferenceRequestDto) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        preferenceService.registerPreference(userId, preferenceRequestDto);
        return ResponseEntity.ok(ApiResponse.success("preferences insert succesfully"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePreference(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody PreferenceRequestDto preferenceRequestDto) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        preferenceService.updatePreference(userId, preferenceRequestDto);
        return ResponseEntity.ok(ApiResponse.success("preferences update succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> fetchPreferences(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        int userId = customOAuth2User.getUserDTO().getUserid();
        List<PreferenceResponseDto> response =  preferenceService.fetchPreferences(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
