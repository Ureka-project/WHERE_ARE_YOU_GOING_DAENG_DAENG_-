package com.daengdaeng_eodiga.project.favorite.dto;

import lombok.*;

@Getter
@Setter
public class FavoriteResponseDto {
    private int favoriteId;
    private int placeId;
    private String name;
    private String placeImage;
    private String placeType;
    private String streetAddresses;
    private Double latitude;
    private Double longitude;
    private String startTime;
    private String endTime;
    private String updatedAt;

    @Builder
    public FavoriteResponseDto(int favoriteId, int placeId, String name, String placeImage, String placeType, String streetAddresses, Double latitude, Double longitude, String startTime, String endTime, String updatedAt) {
        this.favoriteId = favoriteId;
        this.placeId = placeId;
        this.name = name;
        this.placeImage = placeImage;
        this.placeType = placeType;
        this.streetAddresses = streetAddresses;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.updatedAt = updatedAt;
    }
}
