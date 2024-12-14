package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearestPlaceDto {
    private Integer placeId;
    private String name;
    private String city;
    private String cityDetail;
    private String township;
    private Double latitude;
    private Double longitude;
    private String streetAddresses;
    private String telNumber;
    private String url;
    private String placeType;
    private String description;
    private Boolean parking;
    private Boolean indoor;
    private Boolean outdoor;
    private Double distance;
    private Boolean isFavorite;
    private String startTime;
    private String endTime;
    private Double placeScore;
    private String imageurl;
}
