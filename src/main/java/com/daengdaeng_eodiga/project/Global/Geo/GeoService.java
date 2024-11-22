package com.daengdaeng_eodiga.project.Global.Geo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeoService {
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";
    private static final String KAKAO_API_KEY = "KakaoAK {YOUR_REST_API_KEY}";

    public Map<String, String> getRegionInfo(double latitude, double longitude) {
        // API 호출 URL 생성
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", KAKAO_API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // REST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // 결과 파싱
        if (response.getBody() != null) {
            Map<String, Object> firstRegion = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) response.getBody())
                    .get("documents"))
                    .get(0); // 첫 번째 결과

            Map<String, String> result = new HashMap<>();
            result.put("region_1depth_name", firstRegion.get("region_1depth_name").toString()); // 도
            result.put("region_2depth_name", firstRegion.get("region_2depth_name").toString()); // 시/군/구
            result.put("region_3depth_name", firstRegion.get("region_3depth_name").toString()); // 읍/면/동

            return result;
        }

        return null;
    }
}
