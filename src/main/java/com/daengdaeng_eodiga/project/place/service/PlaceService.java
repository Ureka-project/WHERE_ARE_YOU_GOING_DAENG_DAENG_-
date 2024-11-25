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

    public List<PlaceDto> getTopFavoritePlaces() {
        // Repository에서 데이터 조회
        List<Object[]> results = placeRepository.findTopFavoritePlaces();

        // 조회된 데이터를 DTO로 매핑
        return results.stream()
                .map(result -> {
                    PlaceDto dto = new PlaceDto();
                    dto.setPlaceId(result[0] != null ? ((Number) result[0]).intValue() : null);
                    dto.setName(result[1] != null ? result[1].toString() : null);
                    dto.setCity(result[2] != null ? result[2].toString() : null);
                    dto.setCityDetail(result[3] != null ? result[3].toString() : null);
                    dto.setTownship(result[4] != null ? result[4].toString() : null);
                    dto.setLatitude(result[5] != null ? ((Number) result[5]).doubleValue() : null);
                    dto.setLongitude(result[6] != null ? ((Number) result[6]).doubleValue() : null);
                    dto.setStreetAddresses(result[7] != null ? result[7].toString() : null);
                    dto.setTelNumber(result[8] != null ? result[8].toString() : null);
                    dto.setUrl(result[9] != null ? result[9].toString() : null);
                    dto.setPlaceType(result[10] != null ? result[10].toString() : null);
                    dto.setDescription(result[11] != null ? result[11].toString() : null);
                    dto.setParking(result[12] != null ? Boolean.parseBoolean(result[12].toString()) : null);
                    dto.setIndoor(result[13] != null ? Boolean.parseBoolean(result[13].toString()) : null);
                    dto.setOutdoor(result[14] != null ? Boolean.parseBoolean(result[14].toString()) : null);
                    dto.setDistance(result[15] != null ? ((Number) result[15]).doubleValue() : null);
                    dto.setIsFavorite(result[16] != null ? ((Number) result[16]).intValue() == 1 : null);
                    dto.setStartTime(result[17] != null ? result[17].toString() : null);
                    dto.setEndTime(result[18] != null ? result[18].toString() : null);
                    dto.setFavoriteCount(result[19] != null ? ((Number) result[19]).intValue() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
