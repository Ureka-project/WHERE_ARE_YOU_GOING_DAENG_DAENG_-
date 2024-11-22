package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/filter")
    public ResponseEntity<List<PlaceDto>> filterPlaces(@RequestBody FilterRequest request) {
        List<PlaceDto> places = placeService.filterPlaces(
                request.getCity(),
                request.getPlaceType(),
                request.getLatitude(),
                request.getLongitude(),
                request.getUserId()
        );
        return ResponseEntity.ok(places);
    }

    @PostMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchPlaces(@RequestBody SearchRequest request) {
        List<PlaceDto> places = placeService.searchPlaces(
                request.getKeyword(),
                request.getLatitude(),
                request.getLongitude(),
                request.getUserId()
        );
        return ResponseEntity.ok(places);
    }

    // FilterRequest 내부 클래스
    @Getter
    @Setter
    static class FilterRequest {
        private String city;          // 필터 검색 도시
        private String placeType;     // 필터 검색 장소 유형
        private Double latitude;      // 필터 검색 기준 위도
        private Double longitude;     // 필터 검색 기준 경도
        private int userId;           // 필터 검색 사용자 ID
    }

    // SearchRequest 내부 클래스
    @Getter
    @Setter
    static class SearchRequest {
        private String keyword;       // 키워드 검색어
        private Double latitude;      // 키워드 검색 기준 위도
        private Double longitude;     // 키워드 검색 기준 경도
        private int userId;           // 키워드 검색 사용자 ID
    }
}
