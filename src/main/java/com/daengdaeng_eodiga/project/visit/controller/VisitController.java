package com.daengdaeng_eodiga.project.visit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.visit.dto.PetsAtVisitTime;
import com.daengdaeng_eodiga.project.visit.dto.VisitRequest;
import com.daengdaeng_eodiga.project.visit.dto.VisitResponse;
import com.daengdaeng_eodiga.project.visit.service.VisitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visit")
@RequiredArgsConstructor
public class VisitController {

	private final VisitService visitService;

	@PostMapping("")
	public ResponseEntity<ApiResponse<PetsAtVisitTime>> registerVisits(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody VisitRequest request) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		PetsAtVisitTime response = visitService.registerVisit(userId, request.placeId(), request.petIds(), request.visitAt());
		return ResponseEntity.ok(ApiResponse.success(response));

	}

	@DeleteMapping("/{visitId}")
	public ResponseEntity<ApiResponse<?>> deleteVisits(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@PathVariable int visitId) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		visitService.cancelVisit(userId, visitId);
		return ResponseEntity.ok(ApiResponse.success(null));

	}

	@GetMapping("/place/{placeId}")
	public ResponseEntity<ApiResponse<List<VisitResponse>>> fetchPlaceVisits(@PathVariable int placeId) {
		List<VisitResponse> response = visitService.fetchVisitsByPlace(placeId);
		return ResponseEntity.ok(ApiResponse.success(response));

	}
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<List<PetsAtVisitTime>>> fetchUserVisits(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		List<PetsAtVisitTime> response = visitService.fetchVisitsByUser(userId);
		return ResponseEntity.ok(ApiResponse.success(response));

	}
}
