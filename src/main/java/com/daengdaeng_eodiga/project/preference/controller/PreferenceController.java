package com.daengdaeng_eodiga.project.preference.controller;

import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.service.PreferenceService;
import com.daengdaeng_eodiga.util.ApiUtil.ApiSuccess;
import com.daengdaeng_eodiga.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    // TODO : header 로부터 유저정보 가져오는 걸로 변경해야 함
    private final int hardcodedUserId = 2;

    @PostMapping
    public ApiSuccess<?> addPreference(@Validated @RequestBody PreferenceRequestDto preferenceRequestDto) {
        preferenceService.registerPreference(hardcodedUserId, preferenceRequestDto);
        return ApiUtil.success("preferences insert succesfully");
    }

    @PutMapping
    public ApiSuccess<?> updatePreference(@Validated @RequestBody PreferenceRequestDto preferenceRequestDto) {
        preferenceService.updatePreference(hardcodedUserId, preferenceRequestDto);
        return ApiUtil.success("preferences update succesfully");
    }

    @GetMapping
    public ApiSuccess<?> fetchPreferences(){
        List<PreferenceResponseDto> preferenceResponseDtoList =  preferenceService.fetchPreferences(hardcodedUserId);
        return ApiUtil.success(preferenceResponseDtoList);
    }
}
