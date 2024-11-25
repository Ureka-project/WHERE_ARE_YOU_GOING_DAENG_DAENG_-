package com.daengdaeng_eodiga.project.place.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterRequest {
    private String city;
    private String cityDetail; // 추가
    private String placeType;
    private Double latitude;
    private Double longitude;
    private int userId;
}

