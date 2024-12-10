package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MyStoriesDto {
    private String nickname;
    private List<MyStoryContentDto> content;

    @Builder
    public MyStoriesDto(String nickname, String city, String cityDetail, List<MyStoryContentDto> content) {
        this.nickname = nickname;
        this.content = content;
    }
}
