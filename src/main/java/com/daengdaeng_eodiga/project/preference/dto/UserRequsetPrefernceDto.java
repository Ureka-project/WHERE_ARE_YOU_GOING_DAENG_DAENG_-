package com.daengdaeng_eodiga.project.preference.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRequsetPrefernceDto
{
    private String preferenceTypes;

    public UserRequsetPrefernceDto(String preferenceType) {
        this.preferenceTypes = preferenceType;  // List의 단일 항목을 Set으로 변환
    }
}
