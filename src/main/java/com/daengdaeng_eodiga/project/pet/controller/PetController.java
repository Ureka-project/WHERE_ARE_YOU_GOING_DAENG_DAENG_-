package com.daengdaeng_eodiga.project.pet.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.pet.dto.PetListResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetRegisterDto;
import com.daengdaeng_eodiga.project.pet.dto.PetUpdateDto;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    // TODO : header 로부터 유저정보 가져오는 걸로 변경해야 함
    private final int hardcodedUserId = 3;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerPet(@RequestBody PetRegisterDto requestDto) {
        petService.registerPet(hardcodedUserId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("pet inserted succesfully"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePet(@RequestBody PetUpdateDto updateDto) {
        petService.updatePet(updateDto);
        return ResponseEntity.ok(ApiResponse.success("pet updated succesfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> fetchPetList() {
        List<PetListResponseDto> response = petService.fetchUserPetListDto(hardcodedUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
