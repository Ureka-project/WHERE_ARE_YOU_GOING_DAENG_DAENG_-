package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IndividualStoryContentDto {
    private int storyId;
    private String path;

    @Builder
    public IndividualStoryContentDto(int storyId, String path) {
        this.storyId = storyId;
        this.path = path;
    }
}
