package com.daengdaeng_eodiga.project.visit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daengdaeng_eodiga.project.Global.exception.DuplicatePetException;
import com.daengdaeng_eodiga.project.pet.dto.PetResponse;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;
import com.daengdaeng_eodiga.project.visit.dto.PetsAtVisitTime;
import com.daengdaeng_eodiga.project.visit.dto.VisitInfo;
import com.daengdaeng_eodiga.project.visit.dto.VisitResponse;
import com.daengdaeng_eodiga.project.visit.entity.Visit;
import com.daengdaeng_eodiga.project.visit.entity.VisitPet;
import com.daengdaeng_eodiga.project.visit.repository.VisitPetRepository;
import com.daengdaeng_eodiga.project.visit.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {

	private final VisitRepository visitRepository;
	private final PlaceService placeService;
	private final UserService userService;
	private final PetService petService;
	private final VisitPetRepository visitPetRepository;

	public void registerVisit(int userId, int placeId, List<Integer> petIds, LocalDateTime visitAt) {
		Place place = placeService.findPlace(placeId);
		User user = userService.findUser(userId);
		findVisitPet(placeId, visitAt, petIds);
		Visit visit = Visit.builder()
				.visitAt(visitAt)
				.place(place)
				.user(user)
				.build();


		List<VisitPet> visitPets = petIds.stream()
				.map(petId -> {
					Pet pet = petService.findPet(petId);
					return VisitPet.builder()
							.visit(visit)
							.pet(pet)
							.build();
				})
				.toList();
		visitRepository.save(visit);
		visitPetRepository.saveAll(visitPets);

	}

	public List<VisitResponse> fetchVisitsByPlace(int placeId) {
		Place place = placeService.findPlace(placeId);
		LocalDateTime startDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = startDateTime.plusDays(7).toLocalDate().atStartOfDay();

		List<VisitInfo> visitInfos = visitRepository.findVisitInfoByPlaceId(place.getPlaceId(),startDateTime,endDateTime);

		Map<LocalDateTime,List<PetResponse>> visitMapAtTime = visitInfos.stream()
				.collect(Collectors.groupingBy(VisitInfo::getVisitAt, Collectors.mapping(visitInfo -> new PetResponse(visitInfo.getPetId(), visitInfo.getPetName(), visitInfo.getPetImg()), Collectors.toList())));

		Map<LocalDate,List<LocalDateTime>> visitMapAtDate = visitMapAtTime.keySet().stream()
				.collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.toList()));

		List<VisitResponse> visitResponses = visitMapAtDate.entrySet().stream()
				.map(entry -> {
					LocalDate date = entry.getKey();
					List<LocalDateTime> times = entry.getValue();
					List<PetsAtVisitTime> petsAtVisitTimes = times.stream()
							.map(time -> new PetsAtVisitTime(time, visitMapAtTime.get(time)))
						.sorted(Comparator.comparing(petsAtVisitTime -> petsAtVisitTime.visitAt()))
						.toList();
					return new VisitResponse(date, petsAtVisitTimes);
				})
			.sorted(Comparator.comparing(visitResponse -> visitResponse.visitDate())).toList();

		return visitResponses;
	}

	public void findVisitPet(int placeId, LocalDateTime visitAt, List<Integer> petIds) {
		List<VisitPet> visitPets = visitPetRepository.findByPlaceIdAndVisitAtInPetIds(placeId, visitAt, petIds);
		if(!visitPets.isEmpty()){
			throw new DuplicatePetException();
		}
	}



}
