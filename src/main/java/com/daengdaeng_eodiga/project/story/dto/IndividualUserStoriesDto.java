package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IndividualUserStoriesDto {
    private String nickname;
    private String city;
    private String cityDetail;
    private List<IndividualStoryContentDto> content;

    @Builder
    public IndividualUserStoriesDto(String nickname, String city, String cityDetail, List<IndividualStoryContentDto> content) {
        this.nickname = nickname;
        this.city = city;
        this.cityDetail = cityDetail;
        this.content = content;
    }
}
