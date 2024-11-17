package com.daengdaeng_eodiga.project.review.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.review.dto.ReviewRegisterRequest;
import com.daengdaeng_eodiga.project.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	@PostMapping("/api/v1/review")
	public void registerReview(@RequestBody ReviewRegisterRequest request) {
		reviewService.registerReview(request, 1); //TODO : user 시큐리티 기능 완성되면 userId 주입 수정

	}
}
