package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.place.dto.FilterRequest;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.SearchRequest;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> filterPlaces(@RequestBody FilterRequest request) {
        List<PlaceDto> places = placeService.filterPlaces(
                request.getCity(),
                request.getCityDetail(),
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
}
