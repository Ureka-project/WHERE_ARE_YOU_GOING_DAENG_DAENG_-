package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.PlaceDtoMapper;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceScoreRepository placeScoreRepository;

    public List<PlaceDto> filterPlaces(String city, String placeType, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByFiltersAndLocationWithFavorite(city, placeType, latitude, longitude, userId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByKeywordAndLocationWithFavorite(keyword, latitude, longitude, userId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public Place findPlace(int placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    public Double findPlaceScore(int placeId) {
        return placeScoreRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new).getScore();
    }
    public List<PlaceDto> RecommendPlaces(String MyPlace,double latitude, double longitude) {
        List<PlaceDto> RetPalceDto= new ArrayList<>();
        System.out.println(MyPlace);

        String region1 = getRegionValue(MyPlace, "region_1depth_name");
        String region2 = getRegionValue(MyPlace, "region_2depth_name");
        String region3 = getRegionValue(MyPlace, "region_3depth_name");

        return RetPalceDto;
    }
    private String getRegionValue(String roadName, String regionKey) {
        // regionKey 형태: region_1depth_name, region_2depth_name, region_3depth_name
        // 예시: "region_1depth_name=강원특별자치도"
        String pattern = regionKey + "=(.*?),"; // region_Xdepth_name=값, 형식에 맞는 정규 표현식
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(roadName);

        if (m.find()) {
            return m.group(1); // 일치하는 부분의 값을 반환
        }
        return null; // 값이 없으면 null 반환
    }
}
