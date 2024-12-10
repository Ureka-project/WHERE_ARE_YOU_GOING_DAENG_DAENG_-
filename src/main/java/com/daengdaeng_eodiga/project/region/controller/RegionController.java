package com.daengdaeng_eodiga.project.region.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCity;
import com.daengdaeng_eodiga.project.region.service.RegionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/region")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;
	@GetMapping("/owners")
	public ResponseEntity<ApiResponse<RegionOwnerCity>> fetchRegionOwner() {
		RegionOwnerCity response = regionService.fetchRegionOwners();
		return ResponseEntity.ok(ApiResponse.success(response) );
	}

}
