package com.daengdaeng_eodiga.project.review.dto;

import java.time.LocalDate;
import java.util.List;

public record ReviewRegisterRequest(int placeId, String content, int score, List<String> media, List<String> keywords, LocalDate visitedAt) {
}
