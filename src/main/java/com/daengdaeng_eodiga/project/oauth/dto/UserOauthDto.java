package com.daengdaeng_eodiga.project.oauth.dto;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOauthDto {

    private String role;
    private String name;
    private String email;
    private Integer userid;
    private OauthProvider provider;

}
