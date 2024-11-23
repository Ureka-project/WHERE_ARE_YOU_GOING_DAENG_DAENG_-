package com.daengdaeng_eodiga.project.Global.Geo;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoGeoApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GeoService {
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
    private static final String KAKAO_API_KEY = "KakaoAK 91be30bd77f971e11d2e95354debb20c";


    public String getRegionInfo(double latitude, double longitude) {
        // API 호출 URL 생성
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", KAKAO_API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // REST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoGeoApiDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoGeoApiDto.class);
        // 결과 파싱
        if (response.getBody() != null) {
            String result = getMaps(response);

            return result;
        }

        return null;
    }

    private static String getMaps(ResponseEntity<KakaoGeoApiDto> response) {
        List<KakaoGeoApiDto.Document> addressInfoList = Objects.requireNonNull(response.getBody()).getDocuments();
        String ret=addressInfoList.get(0).getAddress() != null? addressInfoList.get(0).getAddress().toString() : "";
        return ret;
    }


}
