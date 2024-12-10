package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyStoryContentDto {
    private int storyId;
    private String city;
    private String cityDetail;
    private String path;

    @Builder
    public MyStoryContentDto(int storyId, String city, String cityDetail, String path) {
        this.storyId = storyId;
        this.city = city;
        this.cityDetail = cityDetail;
        this.path = path;
    }
}
