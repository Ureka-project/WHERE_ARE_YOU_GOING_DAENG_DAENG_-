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
    public List<PlaceDto> filterPlaces(String city, String placeType, Double latitude, Double longitude) {
        List<Object[]> results = placeRepository.findByFiltersAndLocationWithCode(city, placeType, latitude, longitude);

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
            dto.setDistance((Double) result[15]); // 거리
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude) {
        List<Object[]> results = placeRepository.findByKeywordAndLocation(keyword, latitude, longitude);

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
            dto.setDistance((Double) result[15]); // 거리
            return dto;
        }).collect(Collectors.toList());
    }
}
