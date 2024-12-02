package com.daengdaeng_eodiga.project.oauth.dto;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignUpForm {
    private String nickname;

    private String email;

    private String gender;

    private String city;

    private String cityDetail;

    private OauthProvider oauthProvider;

    private boolean pushAgreement;
}