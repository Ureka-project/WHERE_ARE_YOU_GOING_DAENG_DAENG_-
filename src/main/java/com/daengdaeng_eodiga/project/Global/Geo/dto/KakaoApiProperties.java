package com.daengdaeng_eodiga.project.Global.Geo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoApiProperties {
    private String url;
    private String key;
    private String Nopeurl;
}