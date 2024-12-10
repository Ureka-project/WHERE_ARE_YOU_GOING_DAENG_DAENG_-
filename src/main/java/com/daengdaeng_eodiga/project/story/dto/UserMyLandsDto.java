package com.daengdaeng_eodiga.project.story.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserMyLandsDto {
    private String nickname;
    private List<MyLandsDto> lands;

    @Builder
    public UserMyLandsDto(String nickname, List<MyLandsDto> lands) {
        this.nickname = nickname;
        this.lands = lands;
    }
}
