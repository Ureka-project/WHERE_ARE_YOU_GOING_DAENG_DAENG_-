package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VisitRequest(
	@Min(1)
	int placeId,

	@Size(min = 1, message = "반려동물은 최소 1마리 이상이어야 합니다.")
	List<Integer> petIds,
	@NotNull(message = "방문 시간은 필수입니다.")
	LocalDateTime visitAt) {
}
