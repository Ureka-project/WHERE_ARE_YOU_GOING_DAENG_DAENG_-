package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VisitRequest(int placeId, List<Integer> petIds, LocalDateTime visitAt) {
}
