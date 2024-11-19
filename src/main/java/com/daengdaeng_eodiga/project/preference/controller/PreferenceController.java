package com.daengdaeng_eodiga.project.preference.controller;

import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.service.PreferenceService;
import com.daengdaeng_eodiga.util.ApiUtil.ApiSuccess;
import com.daengdaeng_eodiga.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    @PostMapping
    public ApiSuccess<?> addPreference(@Validated @RequestBody PreferenceRequestDto preferenceRequestDto) {
        preferenceService.registerPreference(preferenceRequestDto);
        return ApiUtil.success("preference insert succesfully");
    }
}
