package com.daengdaeng_eodiga.project.story.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.story.dto.*;
import com.daengdaeng_eodiga.project.story.service.StoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/story")
public class StoryController {
    private final StoryService storyService;

    @GetMapping("/mylands")
    public ResponseEntity<ApiResponse<UserMyLandsDto>> fetchUserLands(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        int userId = customOAuth2User.getUserDTO().getUserid();
        UserMyLandsDto response = storyService.fetchUserLands(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> registerStory(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody StoryRequestDto storyRequestDto
    ){
        int userId = customOAuth2User.getUserDTO().getUserid();
        storyService.registerStory(userId,storyRequestDto);
        return ResponseEntity.ok(ApiResponse.success("story inserted successfully"));
    }

    // TODO : 전체 유저 스토리 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupedUserStoriesDto>>> fetchGroupedUserStories(){
        List<GroupedUserStoriesDto> response = storyService.fetchGroupedUserStories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/mystory")
    public ResponseEntity<ApiResponse<MyStoriesDto>> fetchMyStories(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        MyStoriesDto response = storyService.fetchMyStories(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{landOwnerId}")
    public ResponseEntity<ApiResponse<IndividualUserStoriesDto>> fetchIndividualUserStories(
            @PathVariable int landOwnerId,
            @RequestParam String city,
            @RequestParam String cityDetail
    ){
        IndividualUserStoriesDto response = storyService.fetchIndividualUserStories(landOwnerId, city, cityDetail);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{storyId}/viewed")
    public ResponseEntity<ApiResponse<String>> viewStory(
            @Min (1) @PathVariable int storyId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        int userId = customOAuth2User.getUserDTO().getUserid();
        storyService.viewStory(storyId,userId);
        return ResponseEntity.ok(ApiResponse.success("story viewed successfully"));
    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<ApiResponse<String>> deleteStory(
            @Min (1) @PathVariable int storyId
    ){
        storyService.deleteStory(storyId);
        return ResponseEntity.ok(ApiResponse.success("story deleted successfully"));
    }
}
