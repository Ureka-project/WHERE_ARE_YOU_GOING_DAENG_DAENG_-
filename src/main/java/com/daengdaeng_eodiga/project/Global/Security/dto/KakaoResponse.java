package com.daengdaeng_eodiga.project.Global.Security.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        // 내려온 데이터를 바로 필드에 저장
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        if (attributes != null && attributes.containsKey("id")) {
            return attributes.get("id").toString(); // 루트 레벨의 "id" 반환
        }
        return null;
    }

    @Override
    public String getEmail() {
        if (attributes != null) {
            // "kakao_account" 내부에서 "email" 추출
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                return (String) kakaoAccount.get("email");
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (attributes != null) {
            // "properties" 내부에서 "nickname" 추출
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            if (properties != null && properties.containsKey("nickname")) {
                return (String) properties.get("nickname");
            }
        }
        return null;
    }
}
