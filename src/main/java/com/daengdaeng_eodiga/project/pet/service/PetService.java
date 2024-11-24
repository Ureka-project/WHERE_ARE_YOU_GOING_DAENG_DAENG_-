package com.daengdaeng_eodiga.project.pet.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.common.repository.CommonCodeRepository;
import com.daengdaeng_eodiga.project.pet.dto.PetDetailResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetListResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetRegisterDto;
import com.daengdaeng_eodiga.project.pet.dto.PetUpdateDto;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.repository.PetRepository;
import com.daengdaeng_eodiga.project.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
	private final PetRepository petRepository;
	private final UserRepository userRepository;
	private final CommonCodeRepository commonCodeRepository;

	/**
	 * 반려동물 조회 메소드.
	 * List<Pet>타입을 반환
	 * @param user
	 * @return Pet
	 */
	public List<Pet> fetchUserPets(User user) {
		return petRepository.findAllByUser(user);
	}
	public List<Pet> confirmUserPet(User user, Set<Integer> pets) {
		List<Pet> userPets = fetchUserPets(user);
		List<Integer> userPetIds = userPets.stream().map(Pet::getPetId).toList();
		pets.stream().filter(pet -> !userPetIds.contains(pet)).findAny().ifPresent(pet -> {
			throw new PetNotFoundException();
		});
		return userPets;
	}

	public void registerPet(int userId, PetRegisterDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(UserNotFoundException::new);

		Pet pet = Pet.builder()
				.name(requestDto.getName())
				.image(requestDto.getImage())
				.species(requestDto.getSpecies())
				.gender(requestDto.getGender())
				.size(requestDto.getSize())
				.birthday(parseDate(requestDto.getBirthday()))
				.neutering(requestDto.getNeutering())
				.user(user)
				.build();

		petRepository.save(pet);
	}

	public void updatePet(PetUpdateDto updateDto) {
		Pet pet = petRepository.findById(updateDto.getPetId())
				.orElseThrow(PetNotFoundException::new);

		pet.setName(updateDto.getName());
		pet.setImage(updateDto.getImage());
		pet.setSpecies(updateDto.getSpecies());
		pet.setGender(updateDto.getGender());
		pet.setSize(updateDto.getSize());
		pet.setBirthday(parseDate(updateDto.getBirthday()));
		pet.setNeutering(updateDto.getNeutering());

		petRepository.save(pet);
	}

	/**
	 * 유저페이지에서의 반려동물 목록 조회 메소드.
	 * @param userId
	 * @return PetListResponseDto
	 */
	public List<PetListResponseDto> fetchUserPetListDto(int userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<Pet> pets = petRepository.findAllByUser(user);

		return pets.stream()
				.map(pet -> PetListResponseDto.builder()
						.petId(pet.getPetId())
						.name(pet.getName())
						.image(pet.getImage())
						.species(getCommonCodeName(pet.getSpecies()))
						.gender(getCommonCodeName(pet.getGender()))
						.size(getCommonCodeName(pet.getSize()))
						.build())
				.collect(Collectors.toList());
	}

	public PetDetailResponseDto fetchPetDetail(int petId) {

		Pet pet = petRepository.findById(petId)
				.orElseThrow(PetNotFoundException::new);

		String speciesName = getCommonCodeName(pet.getSpecies());
		String genderName = getCommonCodeName(pet.getGender());
		String sizeName = getCommonCodeName(pet.getSize());

		return PetDetailResponseDto.builder()
				.petId(pet.getPetId())
				.name(pet.getName())
				.image(pet.getImage())
				.species(speciesName)
				.gender(genderName)
				.birthday(new SimpleDateFormat("yyyy-MM-dd").format(pet.getBirthday()))
				.neutering(pet.getNeutering())
				.size(sizeName)
				.build();
	}

	public void deletePet(int userId, int petId) {

		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
		if (!pet.getUser().equals(user)) { throw new UserUnauthorizedException();}

		petRepository.delete(pet);
	}

	/**
	 * 공통 코드를 이름으로 변환하는 메소드
	 * @param codeId
	 * @return CommonName
	 */
	private String getCommonCodeName(String codeId) {
		return commonCodeRepository.findByCodeId(codeId)
				.map(commonCode -> commonCode.getName())
				.orElseThrow(CommonCodeNotFoundException::new);
	}

	/**
	 * 날짜 변환 메소드
	 * String 타입의 date를 Date 타입으로 변경
	 * @param date
	 * @return Date
	 */
	private Date parseDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			throw new DateNotFoundException();
		}
	}
}