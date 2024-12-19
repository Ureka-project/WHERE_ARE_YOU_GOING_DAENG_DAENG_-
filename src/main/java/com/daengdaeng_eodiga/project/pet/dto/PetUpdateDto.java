package com.daengdaeng_eodiga.project.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PetUpdateDto {

    @NotBlank(message = "반려동물 이름이 필요함")
    private String name;

    private String image;

    @NotBlank(message = "종 공통코드가 필요함")
    private String species;

    @NotNull(message = "성별이 필요함")
    private String gender;

    @NotBlank(message = "크기가 필요함")
    private String size;

    @NotNull(message = "생일이 필요함")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "생일은 yyyy-MM-dd 형식이어야 합니다.")
    private String birthday;

    @NotNull(message = "중성화 여부가 필요함")
    private Boolean neutering;

    @Builder
    public PetUpdateDto(String name, String image, String species, String gender, String size, String birthday, Boolean neutering) {
        this.name = name;
        this.image = image;
        this.species = species;
        this.gender = gender;
        this.size = size;
        this.birthday = birthday;
        this.neutering = neutering;
    }
    public PetUpdateDto() {}
}
