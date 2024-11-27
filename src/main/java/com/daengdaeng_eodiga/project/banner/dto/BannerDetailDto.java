package com.daengdaeng_eodiga.project.banner.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BannerDetailDto {
    private String eventImage;
    private String eventName;
    private String placeName;
    private String eventDescription;
    private String startDate;
    private String endDate;

    @Builder
    public BannerDetailDto(String eventImage, String eventName, String placeName, String eventDescription, String startDate, String endDate) {
        this.eventImage = eventImage;
        this.eventName = eventName;
        this.placeName = placeName;
        this.eventDescription = eventDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}