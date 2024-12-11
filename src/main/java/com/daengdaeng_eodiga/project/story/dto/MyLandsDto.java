package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

import com.daengdaeng_eodiga.project.region.dto.CityDetailVisit;

@Getter
public class MyLandsDto {
    private String city;
    private List<CityDetailVisit> cityDetails;

    @Builder
    public MyLandsDto(String city, List<CityDetailVisit> cityDetails) {
        this.city = city;
        this.cityDetails = cityDetails;
    }
}
