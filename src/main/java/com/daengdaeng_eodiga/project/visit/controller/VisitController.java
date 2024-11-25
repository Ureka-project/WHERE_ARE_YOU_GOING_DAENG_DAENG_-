package com.daengdaeng_eodiga.project.visit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<ApiResponse<?>> registerVisits(@RequestBody VisitRequest request) {
		//TODO: 시큐리티 기능 완료되면 userId를 받아와서 사용
		visitService.registerVisit(1, request.placeId(), request.petIds(), request.visitAt());
		return ResponseEntity.ok(ApiResponse.success(null));

	}

	@GetMapping("/place/{placeId}")
	public ResponseEntity<ApiResponse<List<VisitResponse>>> fetchPlaceVisits(@PathVariable int placeId) {
		List<VisitResponse> response = visitService.fetchVisitsByPlace(placeId);
		return ResponseEntity.ok(ApiResponse.success(response));

	}
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<List<PetsAtVisitTime>>> fetchUserVisits() {
		//TODO: 시큐리티 기능 완료되면 userId를 받아와서 사용
		List<PetsAtVisitTime> response = visitService.fetchVisitsByUser(1);
		return ResponseEntity.ok(ApiResponse.success(response));

	}
}
