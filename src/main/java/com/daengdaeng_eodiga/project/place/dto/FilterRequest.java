package com.daengdaeng_eodiga.project.place.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class FilterRequest {
    private String city;
    private String cityDetail;
    private String placeType;

    @NotNull(message = "위도를 입력해주세요.")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private Double longitude;
}
