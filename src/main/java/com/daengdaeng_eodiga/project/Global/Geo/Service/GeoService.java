package com.daengdaeng_eodiga.project.Global.Geo.Service;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoApiProperties;
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


    private final KakaoApiProperties kakaoApiProperties;
    public String getRegionInfo(double latitude, double longitude) {
        String url = kakaoApiProperties.getUrl() + "?x=" + longitude + "&y=" + latitude;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiProperties.getKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoGeoApiDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoGeoApiDto.class);
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
