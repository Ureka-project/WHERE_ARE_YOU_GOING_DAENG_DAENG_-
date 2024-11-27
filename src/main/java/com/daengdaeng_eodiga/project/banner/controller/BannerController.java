package com.daengdaeng_eodiga.project.banner.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.banner.dto.BannerDetailDto;
import com.daengdaeng_eodiga.project.banner.dto.BannerListDto;
import com.daengdaeng_eodiga.project.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerListDto>>> fetchBannerList() {
        List<BannerListDto> response = bannerService.fetchBannerList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<BannerDetailDto>> fetchBannerDetail(@PathVariable int eventId) {
        BannerDetailDto response = bannerService.fetchBannerDetail(eventId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}