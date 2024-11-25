package com.daengdaeng_eodiga.project.visit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.visit.entity.VisitPet;

public interface VisitPetRepository extends JpaRepository<VisitPet, Integer> {

	@Query("SELECT vp FROM VisitPet vp WHERE vp.visit.place.placeId = :placeId and vp.visit.visitAt = :visitAt and vp.pet.petId in :petIds")
	List<VisitPet> findByPlaceIdAndVisitAtInPetIds(int placeId, LocalDateTime visitAt,List<Integer> petIds);
}