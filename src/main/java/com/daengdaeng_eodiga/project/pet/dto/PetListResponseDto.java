package com.daengdaeng_eodiga.project.pet.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PetListResponseDto {
    private int petId;
    private String name;
    private String image;
    private String species;
    private String gender;
    private String size;

    @Builder
    public PetListResponseDto(int petId, String name, String image, String species, String gender, String size) {
        this.petId = petId;
        this.name = name;
        this.image = image;
        this.species = species;
        this.gender = gender;
        this.size = size;
    }
}
