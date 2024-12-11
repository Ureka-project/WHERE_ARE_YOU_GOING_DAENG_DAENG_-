package com.daengdaeng_eodiga.project.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RealTimeReviewRequest {

    @NotNull(message = "장소 ID는 필수입니다.")
    private Integer placeId;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;
}
