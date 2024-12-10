package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MyLandsDto {
    private String city;
    private List<String> cityDetail;

    @Builder
    public MyLandsDto(String city, List<String> cityDetail) {
        this.city = city;
        this.cityDetail = cityDetail;
    }
}
