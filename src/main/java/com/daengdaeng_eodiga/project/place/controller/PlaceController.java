package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.Global.Geo.Service.GeoService;
import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.place.dto.FilterRequest;
import com.daengdaeng_eodiga.project.place.dto.NearestRequest;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.SearchRequest;
import com.daengdaeng_eodiga.project.place.service.OpenAiService;
import com.daengdaeng_eodiga.project.place.service.PlaceService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;
    private final GeoService geoService;
    private final OpenAiService openAiService;

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> filterPlaces(@RequestBody FilterRequest request) {
        List<PlaceDto> places = placeService.filterPlaces(
                request.getCity(),
                request.getCityDetail(),
                request.getPlaceType(),
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> searchPlaces(@RequestBody SearchRequest request) {
        List<PlaceDto> places = placeService.searchPlaces(
                request.getKeyword(),
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceDto>> getPlaceDetails(@PathVariable int placeId) {
        PlaceDto placeDetails = placeService.getPlaceDetails(placeId);
        return ResponseEntity.ok(ApiResponse.success(placeDetails));
    }

    @GetMapping("/topfavorites")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getTopFavoritePlaces() {
        List<PlaceDto> topFavoritePlaces = placeService.getTopFavoritePlaces();
        return ResponseEntity.ok(ApiResponse.success(topFavoritePlaces));
    }

    @PostMapping("/topscore")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getTopScoredPlacesWithinRadius(@RequestBody Map<String, Object> request) {
        Double latitude = (Double) request.get("latitude");
        Double longitude = (Double) request.get("longitude");

        List<PlaceDto> places = placeService.getTopScoredPlacesWithinRadius(latitude, longitude);

        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/nearest")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getNearestPlaces(@RequestBody Map<String, Object> request) {
        Double latitude = (Double) request.get("latitude");
        Double longitude = (Double) request.get("longitude");

        List<PlaceDto> places = placeService.getNearestPlaces(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> RecommendPlaces(double latitude, double longitude,
                                                                       @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(ApiResponse.success(placeService.
                RecommendPlaces(geoService.getRegionInfo(latitude, longitude), latitude, longitude, customOAuth2User.getUserDTO().getEmail()
                )));
    }

    @PostMapping("/{placeId}/reviews/summary")
    public ResponseEntity<ApiResponse<String>> createReviewSummary(@PathVariable int placeId) {
        placeService.generateReviewSummary(placeId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 요약이 성공적으로 생성되었습니다!"));
    }
}
