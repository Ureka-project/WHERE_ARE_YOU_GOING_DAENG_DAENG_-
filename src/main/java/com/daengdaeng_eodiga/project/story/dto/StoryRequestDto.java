package com.daengdaeng_eodiga.project.story.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoryRequestDto {
    private String city;
    private String cityDetail;

    @Size(max = 1000, message = "미디어 길이는 최대 1000자까지 가능합니다.")
    private String path;

    @Builder
    public StoryRequestDto(String path, String city, String cityDetail) {
        this.path = path;
        this.city = city;
        this.cityDetail = cityDetail;
    }
    public StoryRequestDto() {}
}
