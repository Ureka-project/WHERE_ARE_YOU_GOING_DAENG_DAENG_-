package com.daengdaeng_eodiga.project.place.dto;

public class PlaceDtoMapper {

    private PlaceDtoMapper() {

    }

    public static PlaceDto convertToPlaceDto(Object[] result) {
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
        dto.setIsFavorite((result[16] instanceof Number) && ((Number) result[16]).longValue() == 1L);
        return dto;
    }
}
