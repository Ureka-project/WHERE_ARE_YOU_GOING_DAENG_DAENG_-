package com.daengdaeng_eodiga.project.place.controller;

import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
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
                request.getLongitude()
        );
        return ResponseEntity.ok(places);
    }

    @PostMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchPlaces(@RequestBody SearchRequest request) {
        List<PlaceDto> places = placeService.searchPlaces(
                request.getKeyword(),
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.ok(places);
    }

    static class FilterRequest {
        private String city;
        private String placeType;
        private Double latitude;
        private Double longitude;

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getPlaceType() { return placeType; }
        public void setPlaceType(String placeType) { this.placeType = placeType; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }

    static class SearchRequest {
        private String keyword;
        private Double latitude;
        private Double longitude;

        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}
