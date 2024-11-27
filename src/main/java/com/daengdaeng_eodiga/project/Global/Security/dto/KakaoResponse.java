package com.daengdaeng_eodiga.project.Global.Security.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        if (attribute != null && attribute.containsKey("response")) {
            this.attribute = (Map<String, Object>) attribute.get("response");
        } else {
            this.attribute = null;  // attribute가 없으면 null로 설정
        }
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        if (attribute != null) {
            return attribute.get("id").toString();
        }
        return null;
    }

    @Override
    public String getEmail() {
        if (attribute != null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
            if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                return (String) kakaoAccount.get("email");
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (attribute != null) {
            Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
            if (properties != null && properties.containsKey("nickname")) {
                return (String) properties.get("nickname");
            }
        }
        return null;
    }
}
