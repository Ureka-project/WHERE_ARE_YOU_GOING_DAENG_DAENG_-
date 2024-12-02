package com.daengdaeng_eodiga.project.preference.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequsetPrefernceDto
{
    @NotBlank(message = "선호 공통코드가 필요함")
    private String preferenceTypes;

    public UserRequsetPrefernceDto(String preferenceType) {
        this.preferenceTypes = preferenceType;
    }
}
