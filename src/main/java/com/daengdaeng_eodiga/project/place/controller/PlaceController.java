package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.Global.Geo.Service.GeoService;
import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.place.dto.FilterRequest;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.SearchRequest;
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
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> filterPlaces(@RequestBody FilterRequest request) {
        List<PlaceDto> places = placeService.filterPlaces(
                request.getCity(),
                request.getPlaceType(),
                request.getLatitude(),
                request.getLongitude(),
                request.getUserId()
        );
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> searchPlaces(@RequestBody SearchRequest request) {
        List<PlaceDto> places = placeService.searchPlaces(
                request.getKeyword(),
                request.getLatitude(),
                request.getLongitude(),
                request.getUserId()
        );
        return ResponseEntity.ok(ApiResponse.success(places));
    }
    @GetMapping("/location")
    public ResponseEntity<ApiResponse<?>> getLocation(@RequestParam double latitude, @RequestParam double longitude  ,
                                                      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        String MyPlace =geoService.getRegionInfo(latitude, longitude);
        List<PlaceDto>RetPalce =placeService.RecommendPlaces(MyPlace,latitude,longitude,customOAuth2User.getUserDTO().getEmail());
        return ResponseEntity.ok(ApiResponse.success(RetPalce));
    }
}
