package com.daengdaeng_eodiga.project.visit.service;

import static java.lang.Thread.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daengdaeng_eodiga.project.Global.exception.DuplicatePetException;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateVisitException;
import com.daengdaeng_eodiga.project.Global.exception.InvalidRequestException;
import com.daengdaeng_eodiga.project.Global.exception.NotFoundException;
import com.daengdaeng_eodiga.project.notification.controller.Publisher;
import com.daengdaeng_eodiga.project.notification.dto.FcmRequestDto;
import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.notification.enums.NotificationTopic;
import com.daengdaeng_eodiga.project.notification.enums.PushType;
import com.daengdaeng_eodiga.project.notification.service.NotificationService;
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
	private final Publisher publisher;
	private final NotificationService notificationService;


	/**
	 * 방문 예정 등록
	 *
	 * @throws DuplicateVisitException : 방문 예정이 겹친 경우
	 * @throws NotFoundException : 유저의 펫이 아닌 경우
	 * @author 김가은
	 * */

	public PetsAtVisitTime registerVisit(int userId, int placeId, List<Integer> petIds, LocalDateTime visitAt) {
		Place place = placeService.findPlace(placeId);
		User user = userService.findUser(userId);
		if(visitAt.getMinute()<30){
			visitAt = visitAt.withMinute(0);
		} else {
			visitAt = visitAt.withMinute(30);
		}
		checkDuplicateVisitAt(placeId, visitAt, user);
		List<Integer> userPets = user.getPets().stream().map(pet -> {
			return pet.getPetId();
		}).toList();

		petIds.forEach(petId -> {
			if(!userPets.contains(petId)){
				throw new NotFoundException("User의 Pet", String.format("PetId %d", petId));
			}
		});

		List<VisitPet> visitPetsAtTime = findVisitPets(place, visitAt,user);
		Visit visit;
		if(visitPetsAtTime.isEmpty()){
			visit = Visit.builder()
					.user(user)
					.place(place)
					.visitAt(visitAt)
					.build();
			visit = visitRepository.save(visit);
		 } else {
			visit = visitPetsAtTime.get(0).getVisit();
		}

		Visit savedVisit = visit;
		List<Integer> visitPetIds = visitPetsAtTime.stream().map(visitPet -> {
			return visitPet.getPet().getPetId();
		}).toList();
		List<Integer> notSavedPetIds = petIds.stream().filter(petId -> !visitPetIds.contains(petId)).toList();


		if(notSavedPetIds.isEmpty()){
			throw new DuplicatePetException();
		}

		List<VisitPet> visitPets = notSavedPetIds.stream()
				.map(petId -> {
					Pet pet = petService.findPet(petId);
					return VisitPet.builder()
							.visit(savedVisit)
							.pet(pet)
							.build();
				})
				.toList();
		List<VisitPet> savedVisitPet = visitPetRepository.saveAll(visitPets);

		return new PetsAtVisitTime(visit.getVisitAt(), savedVisitPet.stream()
				.map(visitPet -> {
					Pet pet = visitPet.getPet();
					return new PetResponse(pet.getPetId(), pet.getName(), pet.getImage());
				})
				.toList(), placeId, visit.getId(),place.getName());

	}

	/**
	 * 방문 예정 일정이 중복되는지 조회
	 *
	 * @throws DuplicateVisitException : 방문 예정이 겹친 경우
	 * @author 김가은
	 * */

	private void checkDuplicateVisitAt(int placeId, LocalDateTime visitAt, User user) {
		visitRepository.findByUserAndVisitAt(user, visitAt, visitAt.plusMinutes(29)).ifPresent(visit -> {
			if(visit.getPlace().getPlaceId() != placeId){
				throw new DuplicateVisitException();
			}
		});
	}

	public void cancelVisit(int userId, int visitId) {
		Optional<Visit> visit = visitRepository.findById(visitId);
		if(visit.isEmpty()){
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

	public List<VisitPet> findVisitPets(Place place, LocalDateTime visitAt,User user) {
		return visitPetRepository.findByPlaceIdAndVisitAt(place, visitAt,user);
	}

	@Async
	public void sendVisitNotification(int userId, int visitId, String petName, String placeName) {
		List<PushToken> pushTokens = fetchVisitPushTokens(userId,visitId);
		List<String> tokens = pushTokens.stream().map(pushToken -> {
			return pushToken.getToken();
		}).toList();
		List<Integer> userIds = pushTokens.stream().map(pushToken -> {
			return pushToken.getUser().getUserId();
		}).toList();
		FcmRequestDto fcmRequestDto = notificationService.createFcmRequest(tokens, userIds, PushType.VISIT, petName, placeName, null,null);
		publisher.publish(NotificationTopic.FCM, fcmRequestDto);
	}

	public List<PushToken> fetchVisitPushTokens(int userId, int visitId) {
		return visitRepository.findPushTokenByVisitId(visitId,userId);
	}



}
