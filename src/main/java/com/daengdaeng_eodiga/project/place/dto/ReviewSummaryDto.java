package com.daengdaeng_eodiga.project.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewSummaryDto {
    private Integer placeId;
    private String goodSummary;
    private String badSummary;
    private LocalDateTime updatedAt;
}
