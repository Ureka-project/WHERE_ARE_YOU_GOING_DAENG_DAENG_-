package com.daengdaeng_eodiga.project.visit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.visit.entity.VisitPet;

public interface VisitPetRepository extends JpaRepository<VisitPet, Integer> {

	@Query("SELECT vp FROM VisitPet vp WHERE vp.visit.place = :place and vp.visit.visitAt = :visitAt and vp.visit.user = :user")
	List<VisitPet> findByPlaceIdAndVisitAt(Place place, LocalDateTime visitAt, User user);
}
