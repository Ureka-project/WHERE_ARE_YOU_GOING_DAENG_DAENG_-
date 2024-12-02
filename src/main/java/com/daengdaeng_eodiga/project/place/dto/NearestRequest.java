package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class NearestRequest {
    @NotBlank(message = "위도는 필수 입력 항목입니다.")
    private Double latitude;

    @NotBlank(message = "경도는 필수 입력 항목입니다.")
    private Double longitude;
}
