package com.daengdaeng_eodiga.project.favorite.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> registerFavorite(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody FavoriteRequestDto favoriteRequestDto) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        FavoriteResponseDto response = favoriteService.registerFavorite(userId, favoriteRequestDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(
            @PathVariable @Min(value = 1, message = "Favorite ID는 1 이상이어야 합니다.") Integer favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok(ApiResponse.success("favorite deleted succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>> fetchFavoriteList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam(name = "lastUpdatedAt", required = false) String lastUpdatedAt,
            @RequestParam(name = "lastFavoriteId", required = false) Integer lastFavoriteId,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        int userId = customOAuth2User.getUserDTO().getUserid();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = (lastUpdatedAt != null && !lastUpdatedAt.isEmpty()) ? LocalDateTime.parse(lastUpdatedAt, formatter) : null;
        Integer parsedId = (lastFavoriteId != null) ? lastFavoriteId : 0;

        List<FavoriteResponseDto> response = favoriteService.fetchFavoriteList(userId, parsedDate, parsedId, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}