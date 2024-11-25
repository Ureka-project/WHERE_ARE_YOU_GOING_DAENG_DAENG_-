package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearestRequest {
    private Double latitude;
    private Double longitude;
    private Integer userId;
}
