package com.daengdaeng_eodiga.project.region.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.region.dto.RegionVisit;
import com.daengdaeng_eodiga.project.region.service.RegionService;
import com.daengdaeng_eodiga.project.story.dto.UserMyLandsDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/region")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;

	@GetMapping("/owners")
	public ResponseEntity<ApiResponse<RegionVisit>> fetchRegionOwner() {
		RegionVisit response = regionService.fetchRegionOwners();
		return ResponseEntity.ok(ApiResponse.success(response) );
	}

	@GetMapping("")
	public ResponseEntity<ApiResponse<UserMyLandsDto>> fetchUserRegion(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	){
		int userId = customOAuth2User.getUserDTO().getUserid();
		UserMyLandsDto response = regionService.fetchUserLands(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@GetMapping("/visitCount")
	public ResponseEntity<ApiResponse<RegionVisit>> fetchUserRegionVisitCount(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	){
		int userId = customOAuth2User.getUserDTO().getUserid();
		RegionVisit response = regionService.fetchUserCityDetailVisitCountForDB(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

}
