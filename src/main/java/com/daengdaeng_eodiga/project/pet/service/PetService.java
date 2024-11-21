package com.daengdaeng_eodiga.project.pet.service;

import java.util.List;
import java.util.Set;

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
}
