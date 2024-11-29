package com.daengdaeng_eodiga.project.user.dto;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.place.entity.Visited;
import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class UserDto {
    private Integer userId;
    private String nickname;
    private String gender;
    private String email;
    private String city;
    private String cityDetail;
    private LocalDateTime createdAt;
    private Boolean pushAgreement;
    private OauthProvider oauthProvider;
}
