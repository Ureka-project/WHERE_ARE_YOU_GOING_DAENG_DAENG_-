package com.daengdaeng_eodiga.project.pet.dto;

import lombok.Getter;

@Getter
public class PetUpdateDto {
    private String name;
    private String image;
    private String species;
    private String gender;
    private String size;
    private String birthday;
    private Boolean neutering;
}
