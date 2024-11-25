package com.daengdaeng_eodiga.project.visit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.visit.dto.VisitInfo;
import com.daengdaeng_eodiga.project.visit.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	@Query("SELECT v.visitAt as visitAt, vp.pet.petId as petId, vp.pet.image as petImg, vp.pet.name as petName, v.place.placeId as placeId, v.id as visitId, v.place.name as placeName FROM Visit v LEFT JOIN VisitPet vp on vp.visit.id = v.id WHERE v.place.placeId = :placeId and  v.visitAt BETWEEN :startDateTime AND :endDateTime")
	List<VisitInfo> findVisitInfoByPlaceId(int placeId, LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Query("SELECT v.visitAt as visitAt, vp.pet.petId as petId, vp.pet.image as petImg, vp.pet.name as petName, v.place.placeId as placeId, v.place.name as placeName, v.id as visitId  FROM Visit v LEFT JOIN VisitPet vp on vp.visit.id = v.id WHERE v.user.userId = :userId and v.visitAt >= :startDateTime order by v.visitAt desc")
	List<VisitInfo> findVisitInfoByUserId(int userId, LocalDateTime startDateTime);
}