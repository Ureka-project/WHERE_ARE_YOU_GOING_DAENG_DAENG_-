package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daengdaeng_eodiga.project.pet.dto.PetResponse;

public record PetsAtVisitTime(LocalDateTime visitAt, List<PetResponse> pets, int placeId, Integer visitId,String placeName) {
}
