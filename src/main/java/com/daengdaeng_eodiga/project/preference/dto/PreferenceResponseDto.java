package com.daengdaeng_eodiga.project.preference.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PreferenceResponseDto {
    private String preferenceInfo;
    private Set<String> preferenceTypes;
}
