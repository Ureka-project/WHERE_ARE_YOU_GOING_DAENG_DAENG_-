package com.daengdaeng_eodiga.project.pet.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

	public List<Pet> fetchPetsByUser(User user) {
		return petRepository.findAllByUser(user);
	}
	public Pet findPet(int petId) {
		return petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
	}
	public List<Pet> confirmUserPet(User user, Set<Integer> pets) {
		List<Pet> userPets = fetchPetsByUser(user);
		Map<Integer, Pet> userPetMap = userPets.stream()
			.collect(Collectors.toMap(Pet::getPetId, pet -> pet));

		List<Pet> confirmPets = pets.stream()
			.map(petId -> {
				Pet pet = userPetMap.get(petId);
				if (pet == null) {
					throw new PetNotFoundException();
				}
				return pet;
			})
			.toList();
		return confirmPets;
	}
}
