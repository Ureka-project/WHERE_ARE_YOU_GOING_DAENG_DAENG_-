package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoryRequestDto {
    // TODO : path에 영상 길이 제한 필요함
    private String city;
    private String cityDetail;
    private String path;

    @Builder
    public StoryRequestDto(String path, String city, String cityDetail) {
        this.path = path;
        this.city = city;
        this.cityDetail = cityDetail;
    }
}
