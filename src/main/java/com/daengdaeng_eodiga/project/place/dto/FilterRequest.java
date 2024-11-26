package com.daengdaeng_eodiga.project.place.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterRequest {
    private String city;
    private String cityDetail;
    private String placeType;
    private Double latitude;
    private Double longitude;
    private int userId;
}
