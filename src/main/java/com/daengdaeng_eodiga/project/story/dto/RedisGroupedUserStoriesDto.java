package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisGroupedUserStoriesDto {
    private int landOwnerId;
    private int storyId;
    private String nickname;
    private String city;
    private String cityDetail;
    private String petImage;
    private String storyType;

    @Builder
    public RedisGroupedUserStoriesDto(int landOwnerId, String nickname, String city, String cityDetail, String petImage, String storyType, int storyId) {
        this.landOwnerId = landOwnerId;
        this.nickname = nickname;
        this.city = city;
        this.cityDetail = cityDetail;
        this.petImage = petImage;
        this.storyType = storyType;
        this.storyId = storyId;
    }

    public RedisGroupedUserStoriesDto(){}
}
