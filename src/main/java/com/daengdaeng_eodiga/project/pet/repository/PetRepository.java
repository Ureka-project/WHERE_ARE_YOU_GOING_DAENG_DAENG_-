package com.daengdaeng_eodiga.project.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.user.entity.User;

public interface PetRepository extends JpaRepository<Pet, Integer> {

	List<Pet> findAllByUser(User user);
}
