package com.daengdaeng_eodiga.project.favorite.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> registerFavorite(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody FavoriteRequestDto favoriteRequestDto) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        favoriteService.registerFavorite(userId, favoriteRequestDto);
        return ResponseEntity.ok(ApiResponse.success("favorite inserted succesfully"));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable Integer favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok(ApiResponse.success("favorite deleted succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FavoriteResponseDto>>> fetchFavoriteList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam int page,
            @RequestParam int size) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        Pageable pageable = PageRequest.of(page, size);
        Page<FavoriteResponseDto> response = favoriteService.fetchFavoriteList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
