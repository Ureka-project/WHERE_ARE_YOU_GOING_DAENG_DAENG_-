package com.daengdaeng_eodiga.project.region.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal;
import com.daengdaeng_eodiga.project.user.entity.User;

public interface RegionVisitTotalRepository extends JpaRepository<RegionVisitTotal, Integer> {
	Optional<RegionVisitTotal> findByCityAndCityDetailAndUser(String city, String cityDetail, User user);

	List<RegionVisitTotal> findByUser(User user);
}
