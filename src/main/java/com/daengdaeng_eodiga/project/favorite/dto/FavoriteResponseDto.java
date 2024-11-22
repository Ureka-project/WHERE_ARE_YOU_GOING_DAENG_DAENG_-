package com.daengdaeng_eodiga.project.favorite.dto;

import lombok.*;

@Getter
@Setter
public class FavoriteResponseDto {
    private int favoriteId;
    private int placeId;
    private String name;
    private String streetAddresses;
    private Double latitude;
    private Double longitude;
    private String openHours;

    @Builder
    public FavoriteResponseDto(int favoriteId, int placeId, String name, String streetAddresses, Double latitude, Double longitude, String openHours) {
        this.favoriteId = favoriteId;
        this.placeId = placeId;
        this.name = name;
        this.streetAddresses = streetAddresses;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openHours = openHours;
    }
}