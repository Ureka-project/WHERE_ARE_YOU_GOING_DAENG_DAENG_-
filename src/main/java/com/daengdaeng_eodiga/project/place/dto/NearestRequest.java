package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class NearestRequest {

    private Double latitude;

    private Double longitude;
}
