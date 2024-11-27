package com.daengdaeng_eodiga.project.oauth.dto;

import com.daengdaeng_eodiga.project.oauth.OauthResult;

public record OauthResponse(String email, OauthResult result) {
}
