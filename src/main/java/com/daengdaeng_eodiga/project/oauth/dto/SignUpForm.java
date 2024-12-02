package com.daengdaeng_eodiga.project.oauth.dto;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignUpForm
{
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;

    @NotBlank  (message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해 주세요.")
    private String email;

    @NotBlank   (message = "성별은 필수 입력 항목입니다.")
    private String gender;

    @NotBlank   (message = "도시는 필수 입력 항목입니다.")
    private String city;

    @NotBlank   (message = "상세 주소는 필수 입력 항목입니다.")
    private String cityDetail;

    @NotBlank   (message = "OAuth 제공자는 필수 입력 항목입니다.")
    private OauthProvider oauthProvider;

    private boolean Pushagreement;
}
