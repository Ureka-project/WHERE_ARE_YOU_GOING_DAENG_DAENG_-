package com.daengdaeng_eodiga.project.Global.Redis.Dto;

import com.daengdaeng_eodiga.project.place.dto.PlaceWithScore;
import lombok.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
public class RedisPlaceDto {
    private double latitude;
    private double longitude;
    private String myplace;
    private List<PlaceWithScore> cashingPlaces;

    @JsonCreator
    public RedisPlaceDto(
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude,
            @JsonProperty("myplace") String myplace,
            @JsonProperty("placeWithScore") List<PlaceWithScore> cashingPlaces) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.myplace = myplace;
        this.cashingPlaces = cashingPlaces;
    }


}

