package com.daengdaeng_eodiga.project.visit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.visit.dto.VisitInfo;
import com.daengdaeng_eodiga.project.visit.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	@Query("SELECT v.visitAt as visitAt, vp.pet.petId as petId, vp.pet.image as petImg, vp.pet.name as petName, v.place.placeId as placeId, v.id as visitId, v.place.name as placeName "
		+ "FROM Visit v "
		+ "LEFT JOIN VisitPet vp on vp.visit.id = v.id "
		+ "WHERE v.place.placeId = :placeId "
		+ "and  v.visitAt BETWEEN :startDateTime AND :endDateTime "
		+ "and v.user.deletedAt IS NULL ")
	List<VisitInfo> findVisitInfoByPlaceId(int placeId, LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Query("SELECT v.visitAt as visitAt, vp.pet.petId as petId, vp.pet.image as petImg, vp.pet.name as petName, v.place.placeId as placeId, v.place.name as placeName, v.id as visitId  "
		+ "FROM Visit v LEFT JOIN VisitPet vp on vp.visit.id = v.id "
		+ "WHERE v.user.userId = :userId and v.visitAt >= :startDateTime "
		+ "order by v.visitAt desc")
	List<VisitInfo> findVisitInfoByUserId(int userId, LocalDateTime startDateTime);

	@Query("SELECT pt FROM Visit v " +
		"JOIN v.user u " +
		"JOIN PushToken pt ON pt.user = u " +
		"WHERE v.place = (SELECT v2.place FROM Visit v2 WHERE v2.id = :visitId) " +
		"AND v.visitAt = (SELECT v2.visitAt FROM Visit v2 WHERE v2.id = :visitId)"+
		"AND u.userId != :userId "+
		"AND u.deletedAt IS NULL "
	)
	List<PushToken> findPushTokenByVisitId(int visitId,int userId);

	/**
	 * 동일한 시간에 방문 예정이 등록되어 있는지 조회
	 *
	 * @author 김가은
	 * */

	@Query("SELECT v FROM Visit v WHERE v.visitAt BETWEEN :startTime AND :endTime AND v.user = :user")
	Optional<Visit> findByUserAndVisitAt( User user, LocalDateTime startTime, LocalDateTime endTime);
}
