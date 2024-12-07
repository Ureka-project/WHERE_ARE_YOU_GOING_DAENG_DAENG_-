package com.daengdaeng_eodiga.project.oauth.dto;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignUpForm {
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(max = 20, message = "닉네임은 최대 20자까지 입력 가능합니다.")
    private String nickname;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String gender;
    @NotBlank(message = "도시는 필수 입력 값입니다.")
    private String city;
    @NotBlank(message = "도시 세부 정보는 필수 입력 값입니다.")
    private String cityDetail;
    @NotNull(message = "OAuth 제공자가 있어야합니다.")
    private OauthProvider oauthProvider;

}