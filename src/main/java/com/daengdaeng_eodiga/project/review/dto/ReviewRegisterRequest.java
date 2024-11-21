package com.daengdaeng_eodiga.project.review.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ReviewRegisterRequest(int placeId, String content, int score, List<String> media, Set<String> keywords, LocalDate visitedAt, Set<Integer> pets) {
}
