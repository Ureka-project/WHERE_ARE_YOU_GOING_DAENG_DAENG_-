package com.daengdaeng_eodiga.project.banner.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BannersDto {
    private int eventId;
    private String eventImage;
    private String eventName;
    private String eventDescription;
    private String placeName;
    private String placeAddress;
    private String startDate;
    private String endDate;

    @Builder
    public BannersDto(int eventId, String eventImage, String eventName, String eventDescription, String placeName, String placeAddress, String startDate, String endDate) {
        this.eventId = eventId;
        this.eventImage = eventImage;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}