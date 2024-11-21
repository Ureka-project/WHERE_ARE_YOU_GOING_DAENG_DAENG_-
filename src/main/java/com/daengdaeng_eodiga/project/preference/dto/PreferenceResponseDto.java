package com.daengdaeng_eodiga.project.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PreferenceResponseDto {
    private String preferenceInfo;
    private Set<String> preferenceTypes;

    public PreferenceResponseDto() {}
}
