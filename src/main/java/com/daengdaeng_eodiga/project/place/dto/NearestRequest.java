package com.daengdaeng_eodiga.project.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;




@Data
@AllArgsConstructor
public class NearestRequest {
    @NotNull(message = "위도가 필요함 ")
    private Double latitude;
    @NotNull(message = "경도가 필요함 ")
    private Double longitude;
}
