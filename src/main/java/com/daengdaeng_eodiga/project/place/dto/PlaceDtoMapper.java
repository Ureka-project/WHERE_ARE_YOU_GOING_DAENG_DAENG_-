package com.daengdaeng_eodiga.project.place.dto;

public class PlaceDtoMapper {

    private PlaceDtoMapper() {}

    public static PlaceDto convertToPlaceDto(Object[] result) {
        PlaceDto dto = new PlaceDto();
        try {
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
            dto.setParking(result[12] != null && (Boolean.parseBoolean(result[12].toString()) || result[12].toString().equals("1")));
            dto.setIndoor(result[13] != null && (Boolean.parseBoolean(result[13].toString()) || result[13].toString().equals("1")));
            dto.setOutdoor(result[14] != null && (Boolean.parseBoolean(result[14].toString()) || result[14].toString().equals("1")));
            dto.setDistance(result[15] != null ? ((Number) result[15]).doubleValue() : null);
            dto.setIsFavorite(result[16] != null && Integer.parseInt(result[16].toString()) == 1);
            dto.setStartTime(result[17] != null ? result[17].toString() : null);
            dto.setEndTime(result[18] != null ? result[18].toString() : null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to map result to PlaceDto. Ensure data types are correct.", e);
        }
        return dto;
    }
}
