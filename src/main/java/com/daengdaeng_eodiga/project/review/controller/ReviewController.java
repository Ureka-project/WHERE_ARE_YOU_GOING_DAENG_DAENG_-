package com.daengdaeng_eodiga.project.review.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.daengdaeng_eodiga.project.review.dto.ReviewDto;
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
	public ResponseEntity<ApiResponse<ReviewDto>> registerReview(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody ReviewRegisterRequest request) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		ReviewDto response = reviewService.registerReview(request, userId);
		return ResponseEntity.ok(ApiResponse.success(response));
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
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchUserReviews(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestParam int page,@RequestParam int size) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		ReviewsResponse response = reviewService.fetchUserReviews(userId,page,size);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
