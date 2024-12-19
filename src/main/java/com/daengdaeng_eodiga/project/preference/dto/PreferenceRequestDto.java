package com.daengdaeng_eodiga.project.preference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class PreferenceRequestDto {
    @NotBlank(message = "그룹코드가 필요함")
    private String preferenceInfo;

    @NotEmpty(message = "공통코드 Set이 존재해야 함")
    @Size(min = 1, message = "공통코드는 최소 1개의 값을 가져야 함")
    private Set<String> preferenceTypes;

    public PreferenceRequestDto(String preferenceInfo, Set<String> preferenceTypes) {
        this.preferenceInfo = preferenceInfo;
        this.preferenceTypes = preferenceTypes;
    }
}
