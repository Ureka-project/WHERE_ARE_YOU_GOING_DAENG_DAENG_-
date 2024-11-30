package com.daengdaeng_eodiga.project.banner.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BannerListDto {
    private int eventId;
    private String eventImage;

    @Builder
    public BannerListDto(int eventId, String eventImage) {
        this.eventId = eventId;
        this.eventImage = eventImage;
    }
}