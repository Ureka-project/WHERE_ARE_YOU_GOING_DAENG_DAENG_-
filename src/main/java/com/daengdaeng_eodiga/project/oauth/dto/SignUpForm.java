package com.daengdaeng_eodiga.project.oauth.dto;

import lombok.Data;

@Data
public class SignUpForm
{
    private String nickname;
    private String email;
    private String gender;
    private String city;
    private String cityDetail;
}
