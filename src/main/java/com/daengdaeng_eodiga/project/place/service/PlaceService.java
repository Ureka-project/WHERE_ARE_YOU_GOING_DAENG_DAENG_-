package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.place.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto> filterPlaces(String city, String placeType, Double latitude, Double longitude, int userId);

    List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, int userId);
}
