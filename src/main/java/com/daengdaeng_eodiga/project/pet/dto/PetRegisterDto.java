package com.daengdaeng_eodiga.project.pet.dto;

import lombok.Getter;

@Getter
public class PetRegisterDto {
    private String name;
    private String image;
    private String species;
    private String birthday;
    private String gender;
    private String size;
    private Boolean neutering;
}
