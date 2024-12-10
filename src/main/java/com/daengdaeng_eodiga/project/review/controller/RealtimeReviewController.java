package com.daengdaeng_eodiga.project.review.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.review.dto.RealTimeReviewRequest;
import com.daengdaeng_eodiga.project.review.service.RealtimeReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/review")
@RequiredArgsConstructor
@Validated
public class RealtimeReviewController {

    private final RealtimeReviewService realtimeReviewService;

    @PostMapping("/realtime")
    public ResponseEntity<ApiResponse<String>> checkRealtimeReview(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody RealTimeReviewRequest request) {

        int userId = customOAuth2User.getUserDTO().getUserid();
        realtimeReviewService.checkRealtimeReviewEligibility(request, userId);

        return ResponseEntity.ok(ApiResponse.success("실시간 리뷰 작성이 가능합니다."));
    }
}
