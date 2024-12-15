package com.daengdaeng_eodiga.project.region.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.region.entity.RegionVisitDay;
import com.daengdaeng_eodiga.project.user.entity.User;

public interface RegionVisitDayRepository extends JpaRepository<RegionVisitDay, Integer> {

	@Query("SELECT r FROM RegionVisitDay r WHERE r.city = :city AND r.cityDetail = :cityDetail AND r.user = :user AND r.createdAt >= :startedAt AND r.createdAt < :endedAt")
	Optional<RegionVisitDay> findByCityAndCityDetailAndUserAndCreatedAt(String city,String cityDetail, User user, LocalDateTime startedAt, LocalDateTime endedAt);
}
