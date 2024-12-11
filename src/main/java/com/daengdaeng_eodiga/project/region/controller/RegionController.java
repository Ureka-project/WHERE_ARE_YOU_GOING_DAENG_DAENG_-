package com.daengdaeng_eodiga.project.region.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCity;
import com.daengdaeng_eodiga.project.region.service.RegionService;
import com.daengdaeng_eodiga.project.story.dto.UserMyLandsDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/region")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;

	@GetMapping("/owners")
	public ResponseEntity<ApiResponse<RegionOwnerCity>> fetchRegionOwner() {
		RegionOwnerCity response = regionService.fetchCountVisitAllRegion();
		return ResponseEntity.ok(ApiResponse.success(response) );
	}

	@GetMapping("")
	public ResponseEntity<ApiResponse<UserMyLandsDto>> fetchUserRegion(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	){
		int userId = customOAuth2User.getUserDTO().getUserid();
		UserMyLandsDto response = regionService.fetchUserCityDetail(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

}
