package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VisitRequest(
	@Min(1)
	int placeId,

	List<Integer> petIds,
	@NotNull(message = "방문 시간은 필수입니다.")
	LocalDateTime visitAt) {
}
