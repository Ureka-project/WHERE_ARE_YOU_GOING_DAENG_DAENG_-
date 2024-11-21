package com.daengdaeng_eodiga.project.favorite.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FavoriteResponseDto {
    private int favoriteId;
    private int placeId;
    private String name;
    private String streetAddresses;
    private Double latitude;
    private Double longitude;
    private String openHours;
}
