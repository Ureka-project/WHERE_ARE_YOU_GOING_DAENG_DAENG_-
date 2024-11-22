package com.daengdaeng_eodiga.project.pet.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.daengdaeng_eodiga.project.Global.exception.DateNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.common.repository.CommonCodeRepository;
import com.daengdaeng_eodiga.project.pet.dto.PetRegisterDto;
import com.daengdaeng_eodiga.project.pet.dto.PetUpdateDto;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.PetNotFoundException;
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