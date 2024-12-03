package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.Global.Geo.Service.GeoService;
import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.place.dto.*;
import com.daengdaeng_eodiga.project.place.entity.ReviewSummary;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import com.daengdaeng_eodiga.project.review.repository.ReviewSummaryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
@Validated
public class PlaceController {

    private final PlaceService placeService;
    private final GeoService geoService;
    private final ReviewSummaryRepository reviewSummaryRepository;

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> filterPlaces(@Valid @RequestBody FilterRequest request) {
        List<PlaceDto> places = placeService.filterPlaces(
                request.getCity(),
                request.getCityDetail(),
                request.getPlaceType(),
                request.getLatitude(),
                request.getLongitude(),
                null
        );
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> searchPlaces(@Valid @RequestBody SearchRequest request) {
        List<PlaceDto> places = placeService.searchPlaces(
                request.getKeyword(),
                request.getLatitude(),
                request.getLongitude(),
                null
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
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getTopScoredPlacesWithinRadius(@Valid @RequestBody TopScoreRequest request) {
        List<PlaceDto> places = placeService.getTopScoredPlacesWithinRadius(request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/nearest")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getNearestPlaces(@Valid @RequestBody NearestRequest request) {
        List<PlaceDto> places = placeService.getNearestPlaces(request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<List<PlaceWithScore>>> RecommendPlaces(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                             @Valid @RequestBody NearestRequest request) {
        Integer userId = customOAuth2User.getUserDTO().getUserid();
        String myPlace = geoService.getRegionInfo(request.getLatitude(), request.getLongitude(), userId);
        List<PlaceWithScore> places = placeService.RecommendPlaces(myPlace, request.getLatitude(), request.getLongitude(), userId);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/{placeId}/reviews/summary")
    public ResponseEntity<ApiResponse<String>> createReviewSummary(@PathVariable int placeId) {
        placeService.generateReviewSummary(placeId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 요약이 성공적으로 생성되었습니다!"));
    }

    @GetMapping("/{placeId}/reviews/summary")
    public ResponseEntity<ReviewSummaryDto> getReviewSummary(@PathVariable Integer placeId) {
        ReviewSummary reviewSummary = reviewSummaryRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Review Summary not found"));

        ReviewSummaryDto response = new ReviewSummaryDto(
                reviewSummary.getPlaceId(),
                reviewSummary.getGoodSummary(),
                reviewSummary.getBadSummary(),
                reviewSummary.getUpdateDate()
        );
        return ResponseEntity.ok(response);
    }
}
