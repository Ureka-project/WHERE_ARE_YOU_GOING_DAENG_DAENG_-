package com.daengdaeng_eodiga.project.pet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDetailResponseDto {
    private int petId;
    private String name;
    private String image;
    private String species;
    private String gender;
    private String birthday;
    private Boolean neutering;
    private String size;

    @Builder
    public PetDetailResponseDto(int petId, String name, String image, String species, String gender, String birthday, Boolean neutering, String size) {
        this.petId = petId;
        this.name = name;
        this.image = image;
        this.species = species;
        this.gender = gender;
        this.birthday = birthday;
        this.neutering = neutering;
        this.size = size;
    }
}
