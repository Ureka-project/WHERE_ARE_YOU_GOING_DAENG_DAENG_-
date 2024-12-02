package com.daengdaeng_eodiga.project.Global.Geo.Service;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoApiProperties;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoApiResponseDto;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoGeoApiDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final UserRepository userRepository;
    public String getRegionInfo(double latitude, double longitude,Integer userId)  {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiProperties.getKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        String url = kakaoApiProperties.getUrl() + "?x=" + longitude + "&y=" + latitude;
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

    public List<Object> getNotAgreeInfo(double latitude, double longitude,Integer userId)  {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiProperties.getKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        Optional<User> Ouser = userRepository.findById(userId);
        if (Ouser.isPresent()) {
            User user = Ouser.get();
            String nourl = kakaoApiProperties.getNopeurl() + user.getCity() + " " + user.getCityDetail();
            ResponseEntity<String> response = restTemplate.exchange(nourl, HttpMethod.GET, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoApiResponseDto apiResponseDto = null;

            try {
                apiResponseDto = objectMapper.readValue(response.getBody(), KakaoApiResponseDto.class);

                if (apiResponseDto != null && apiResponseDto.getDocuments() != null && !apiResponseDto.getDocuments().isEmpty()) {
                    KakaoApiResponseDto.Document document = apiResponseDto.getDocuments().get(0);
                    latitude = Double.parseDouble(document.getY());
                    longitude = Double.parseDouble(document.getX());
                    String address = apiResponseDto.getDocuments().get(0).getAddress().getRegion1DepthName() + " " +
                            apiResponseDto.getDocuments().get(0).getAddress().getRegion2DepthName();
                    List<Object> Ret = new ArrayList<>();
                    Ret.add(latitude);
                    Ret.add(longitude);
                    Ret.add(address);
                    return Ret;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


}
