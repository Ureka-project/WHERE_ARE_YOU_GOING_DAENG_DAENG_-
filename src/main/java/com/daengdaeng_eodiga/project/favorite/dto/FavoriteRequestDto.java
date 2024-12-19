package com.daengdaeng_eodiga.project.favorite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FavoriteRequestDto {
    @NotNull(message = "장소 ID가 필요함")
    private Integer placeId;

    public FavoriteRequestDto(Integer placeId) {
        this.placeId = placeId;
    }
    public FavoriteRequestDto() {}
}
