package com.daengdaeng_eodiga.project.review.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRegisterRequest(
	@Min(1)
	int placeId,
	@Size(min = 1, max = 500, message = "리뷰 내용은 1자 이상 500자 이하로 작성해주세요.")
	String content,
	@Min(1)
	@Max(5)
	int score,
	@Size(min = 0, max = 5, message = "사진은 5장 이하로 첨부할 수 있습니다.")
	List<String> media,
	@Size(min = 0, max = 3, message = "키워드는 3개 이하로 작성해주세요.")
	Set<String> keywords,
	@NotNull(message = "방문 날짜는 필수입니다.")
	LocalDate visitedAt,
	Set<Integer> pets,
	@NotNull(message = "리뷰 타입은 필수입니다.")
	String reviewType ) {
}
