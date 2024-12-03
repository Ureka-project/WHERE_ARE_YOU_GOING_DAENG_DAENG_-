package com.daengdaeng_eodiga.project.pet.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.pet.dto.PetDetailResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetListResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetRegisterDto;
import com.daengdaeng_eodiga.project.pet.dto.PetUpdateDto;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Validated
public class PetController {
    private final PetService petService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> registerPet(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody PetRegisterDto requestDto) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        petService.registerPet(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("pet inserted succesfully"));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<ApiResponse<String>> updatePet(
            @PathVariable @Min(value = 1, message = "Pet ID는 1 이상이어야 합니다.") int petId,
            @Valid @RequestBody PetUpdateDto updateDto) {
        petService.updatePet(petId, updateDto);
        return ResponseEntity.ok(ApiResponse.success("pet updated succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PetListResponseDto>>> fetchPetList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        List<PetListResponseDto> response = petService.fetchUserPetListDto(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetDetailResponseDto>> fetchPetDetail(
            @PathVariable @Min(value = 1, message = "Pet ID는 1 이상이어야 합니다.") int petId) {
        PetDetailResponseDto petDetail = petService.fetchPetDetail(petId);
        return ResponseEntity.ok(ApiResponse.success(petDetail));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<ApiResponse<String>> deletePet(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable @Min(value = 1, message = "Pet ID는 1 이상이어야 합니다.") int petId) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        petService.deletePet(userId, petId);
        return ResponseEntity.ok(ApiResponse.success("pet deleted succesfully"));
    }
}
