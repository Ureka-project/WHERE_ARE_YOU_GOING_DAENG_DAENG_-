package com.daengdaeng_eodiga.project.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.review.dto.ReviewRegisterRequest;
import com.daengdaeng_eodiga.project.review.dto.ReviewsResponse;
import com.daengdaeng_eodiga.project.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	@PostMapping("/review")
	public ResponseEntity<ApiResponse<?>> registerReview(@RequestBody ReviewRegisterRequest request) {
		System.out.println(request.keywords().toString());
		reviewService.registerReview(request, 1); //TODO : user 시큐리티 기능 완성되면 userId 주입 수정
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@DeleteMapping("/review/{reviewId}")
	public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable int reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@GetMapping("/reviews/place/{placeId}/{orderType}")
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchPlaceReviews(@PathVariable int placeId, @PathVariable OrderType orderType,@RequestParam int page,@RequestParam int size) {
		ReviewsResponse response = reviewService.fetchPlaceReviews(placeId,page,size, orderType);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@GetMapping("/reviews/user")
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchUserReviews(@RequestParam int page,@RequestParam int size) {
		ReviewsResponse response = reviewService.fetchUserReviews(1,page,size); //TODO : user 시큐리티 기능 완성되면 userId 주입 수정
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
