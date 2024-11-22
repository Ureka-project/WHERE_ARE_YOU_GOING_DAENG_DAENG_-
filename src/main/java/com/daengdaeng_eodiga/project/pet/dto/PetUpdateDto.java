package com.daengdaeng_eodiga.project.pet.dto;

import lombok.Getter;

@Getter
public class PetUpdateDto {
    private int petId;
    private String name;
    private String image;
    private String species; // 공통 코드 ID
    private String gender;  // 공통 코드 ID
    private String size;    // 공통 코드 ID
    private String birthday; // 날짜 문자열
    private Boolean neutering;
}
