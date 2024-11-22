package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    private int placeId;            // 장소 ID
    private String name;            // 장소 이름
    private String city;            // 도시 이름
    private String cityDetail;      // 도시 세부정보
    private String township;        // 읍/면/동
    private Double latitude;        // 위도
    private Double longitude;       // 경도
    private String streetAddresses; // 도로명 주소
    private String telNumber;       // 전화번호
    private String url;             // URL
    private String placeType;       // 장소 유형 (공통코드의 name)
    private String description;     // 설명
    private Boolean parking;        // 주차 가능 여부
    private Boolean indoor;         // 실내 여부
    private Boolean outdoor;        // 실외 여부
    private Double distance;        // 사용자 위치로부터 거리
    private Boolean isFavorite;     // 즐겨찾기 여부
}
