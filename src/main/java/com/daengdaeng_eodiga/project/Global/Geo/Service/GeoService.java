package com.daengdaeng_eodiga.project.Global.Geo.Service;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoApiProperties;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoApiResponseDto;
import com.daengdaeng_eodiga.project.Global.Geo.dto.KakaoGeoApiDto;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.user.service.UserService;
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
    private final UserService   userService;
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
        if(addressInfoList.size()==0 || addressInfoList == null)
            return "";
        String ret=addressInfoList.get(0).getAddress() != null? addressInfoList.get(0).getAddress().toString() : "";
        return ret;
    }

    public List<Object> getNotAgreeInfo(Integer userId)  {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiProperties.getKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        User user = userService.findUser(userId);
        if (user==null)
            throw new UserNotFoundException();


        String nourl = kakaoApiProperties.getNopeurl() + user.getCity() + " " + user.getCityDetail();
        ResponseEntity<String> response = restTemplate.exchange(nourl, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoApiResponseDto apiResponseDto = null;
        try {
            apiResponseDto = objectMapper.readValue(response.getBody(), KakaoApiResponseDto.class);
            if (apiResponseDto != null && apiResponseDto.getDocuments() != null && !apiResponseDto.getDocuments().isEmpty()) {
                List<Object> Ret = getObjects(apiResponseDto);
                return Ret;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }




        return null;
    }

    private static List<Object> getObjects(KakaoApiResponseDto apiResponseDto) {
        KakaoApiResponseDto.Document document = apiResponseDto.getDocuments().get(0);
        Double latitude = Double.parseDouble(document.getY());
        Double longitude = Double.parseDouble(document.getX());
        String address = apiResponseDto.getDocuments().get(0).getAddress().getRegion1DepthName() + " " + apiResponseDto.getDocuments().get(0).getAddress().getRegion2DepthName();
        List<Object> Ret = new ArrayList<>();
        Ret.add(latitude);
        Ret.add(longitude);
        Ret.add(address);
        return Ret;
    }


    public  double calculateDistance(double oldLatitude, double oldLongitude, double newLatitude, double newLongitude) {

        final int R = 6371;

        double lat1 = Math.toRadians(oldLatitude);
        double lon1 = Math.toRadians(oldLongitude);
        double lat2 = Math.toRadians(newLatitude);
        double lon2 = Math.toRadians(newLongitude);


        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;


        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        return distance;
    }


}
