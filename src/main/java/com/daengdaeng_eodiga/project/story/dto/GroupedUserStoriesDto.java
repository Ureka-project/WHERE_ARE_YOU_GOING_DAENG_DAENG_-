package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupedUserStoriesDto {
    private int landOwnerId;
    private String nickname;
    private String city;
    private String cityDetail;
    private String petImage;

    @Builder
    public GroupedUserStoriesDto(int landOwnerId, String nickname, String city, String cityDetail, String petImage) {
        this.landOwnerId = landOwnerId;
        this.nickname = nickname;
        this.city = city;
        this.cityDetail = cityDetail;
        this.petImage = petImage;
    }
}
