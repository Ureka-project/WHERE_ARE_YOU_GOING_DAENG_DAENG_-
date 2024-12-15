package com.daengdaeng_eodiga.project.place.dto;

public class NearestPlaceDtoMapper {

    private NearestPlaceDtoMapper() {}

    public static NearestPlaceDto convertToNearestPlaceDto(Object[] result) {
        NearestPlaceDto dto = new NearestPlaceDto();
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
            dto.setParking(result[12] != null && parseBoolean(result[12]));
            dto.setIndoor(result[13] != null && parseBoolean(result[13]));
            dto.setOutdoor(result[14] != null && parseBoolean(result[14]));
            dto.setDistance(result.length > 15 && result[15] != null ? ((Number) result[15]).doubleValue() : null);
            dto.setIsFavorite(result.length > 16 && result[16] != null && parseBoolean(result[16]));
            dto.setStartTime(result.length > 17 && result[17] != null ? result[17].toString() : null);
            dto.setEndTime(result.length > 18 && result[18] != null ? result[18].toString() : null);
            dto.setPlaceScore(result.length > 19 && result[19] != null ? ((Number) result[19]).doubleValue() : null);
            dto.setImageurl(result.length > 20 && result[20] != null ? result[20].toString() : null);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to map result to NearestPlaceDto. Ensure data types and query structure are correct.", e);
        }
        return dto;
    }

    private static boolean parseBoolean(Object value) {
        return Boolean.parseBoolean(value.toString()) || "1".equals(value.toString());
    }
}
