package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Override
    public List<PlaceDto> filterPlaces(String city, String placeType, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByFiltersAndLocationWithFavorite(city, placeType, latitude, longitude, userId);

        return results.stream().map(result -> {
            PlaceDto dto = new PlaceDto();
            dto.setPlaceId((Integer) result[0]);
            dto.setName((String) result[1]);
            dto.setCity((String) result[2]);
            dto.setCityDetail((String) result[3]);
            dto.setTownship((String) result[4]);
            dto.setLatitude((Double) result[5]);
            dto.setLongitude((Double) result[6]);
            dto.setStreetAddresses((String) result[7]);
            dto.setTelNumber((String) result[8]);
            dto.setUrl((String) result[9]);
            dto.setPlaceType((String) result[10]);
            dto.setDescription((String) result[11]);
            dto.setParking((Boolean) result[12]);
            dto.setIndoor((Boolean) result[13]);
            dto.setOutdoor((Boolean) result[14]);
            dto.setDistance((Double) result[15]);
            // Long 값을 Boolean으로 변환
            dto.setIsFavorite((result[16] instanceof Number) && ((Number) result[16]).longValue() == 1L);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, int userId) {
        List<Object[]> results = placeRepository.findByKeywordAndLocationWithFavorite(keyword, latitude, longitude, userId);

        return results.stream().map(result -> {
            PlaceDto dto = new PlaceDto();
            dto.setPlaceId((Integer) result[0]);
            dto.setName((String) result[1]);
            dto.setCity((String) result[2]);
            dto.setCityDetail((String) result[3]);
            dto.setTownship((String) result[4]);
            dto.setLatitude((Double) result[5]);
            dto.setLongitude((Double) result[6]);
            dto.setStreetAddresses((String) result[7]);
            dto.setTelNumber((String) result[8]);
            dto.setUrl((String) result[9]);
            dto.setPlaceType((String) result[10]);
            dto.setDescription((String) result[11]);
            dto.setParking((Boolean) result[12]);
            dto.setIndoor((Boolean) result[13]);
            dto.setOutdoor((Boolean) result[14]);
            dto.setDistance((Double) result[15]);
            // Long 값을 Boolean으로 변환
            dto.setIsFavorite((result[16] instanceof Number) && ((Number) result[16]).longValue() == 1L);

            return dto;
        }).collect(Collectors.toList());
    }
}
