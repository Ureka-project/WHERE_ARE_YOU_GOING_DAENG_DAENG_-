package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.PlaceDtoMapper;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceScoreRepository placeScoreRepository;

    public List<PlaceDto> filterPlaces(String city, String cityDetail, String placeType, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByFiltersAndLocationWithFavorite(city, cityDetail, placeType, latitude, longitude, userId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }


    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByKeywordAndLocationWithFavorite(keyword, latitude, longitude, userId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public PlaceDto getPlaceDetails(int placeId) {

        List<Object[]> results = placeRepository.findPlaceDetailsById(placeId);

        if (results.isEmpty()) {
            throw new PlaceNotFoundException();
        }

        return PlaceDtoMapper.convertToPlaceDto(results.get(0));
    }

    public Place findPlace(int placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    public Double findPlaceScore(int placeId) {
        return placeScoreRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new).getScore();
    }
}
