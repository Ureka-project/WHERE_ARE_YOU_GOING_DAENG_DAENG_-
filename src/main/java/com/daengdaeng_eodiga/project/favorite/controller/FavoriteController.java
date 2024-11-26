package com.daengdaeng_eodiga.project.favorite.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // TODO : 추후에 유저 ID 변경하기
    int userId = 4;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> registerFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
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
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FavoriteResponseDto> response = favoriteService.fetchFavoriteList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
