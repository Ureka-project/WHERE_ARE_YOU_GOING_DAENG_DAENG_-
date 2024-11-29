package com.daengdaeng_eodiga.project.visit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daengdaeng_eodiga.project.Global.exception.DuplicatePetException;
import com.daengdaeng_eodiga.project.Global.exception.InvalidRequestException;
import com.daengdaeng_eodiga.project.Global.exception.NotFoundException;
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

	public void cancelVisit(int userId, int visitId) {
		Optional<Visit> visit = visitRepository.findById(visitId);
		if(visit.isEmpty()){
			System.out.println("visitId : " + visitId);
			throw new NotFoundException("Visit", String.format("VisitId %d", visitId));
		}
		if(visit.get().getUser().getUserId() != userId){
			throw new InvalidRequestException("Visit", String.format("UserId %d", userId));
		}
		visitRepository.deleteById(visitId);
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
							.map(time -> {
								List<PetResponse> petResponses = visitMapAtTime.get(time);
								return new PetsAtVisitTime(time, petResponses, place.getPlaceId(), null,place.getName());
							})
						.sorted(Comparator.comparing(petsAtVisitTime -> petsAtVisitTime.visitAt()))
						.toList();
					return new VisitResponse(date, petsAtVisitTimes);
				})
			.sorted(Comparator.comparing(visitResponse -> visitResponse.visitDate())).toList();

		return visitResponses;
	}

	public List<PetsAtVisitTime> fetchVisitsByUser(int userId) {
		LocalDateTime startDateTime = LocalDateTime.now();
		List<VisitInfo> visitInfos = visitRepository.findVisitInfoByUserId(userId,startDateTime);

		Map<Integer,List<PetResponse>> pets = new HashMap<>();
		Map<Integer,Integer> places = new HashMap<>();
		Map<Integer,String> placeNames = new HashMap<>();
		Map<Integer,LocalDateTime> visitTimes = new HashMap<>();
		visitInfos.forEach(visitInfo -> {
			int visitId = visitInfo.getVisitId();
			List<PetResponse> petResponse = pets.getOrDefault(visitId, new ArrayList<>());
			petResponse.add(new PetResponse(visitInfo.getPetId(), visitInfo.getPetName(), visitInfo.getPetImg()));
			pets.put(visitId, petResponse);
			places.put(visitId, visitInfo.getPlaceId());
			placeNames.put(visitId, visitInfo.getPlaceName());
			visitTimes.put(visitId, visitInfo.getVisitAt());
		});
		return visitTimes.keySet().stream()
			.map(visitId -> new PetsAtVisitTime(visitTimes.get(visitId), pets.get(visitId), places.get(visitId), visitId,placeNames.get(visitId)))
			.sorted(Comparator.comparing(PetsAtVisitTime::visitAt).reversed())
			.toList();
	}

	public void findVisitPet(int placeId, LocalDateTime visitAt, List<Integer> petIds) {
		List<VisitPet> visitPets = visitPetRepository.findByPlaceIdAndVisitAtInPetIds(placeId, visitAt, petIds);
		if(!visitPets.isEmpty()){
			throw new DuplicatePetException();
		}
	}



}
