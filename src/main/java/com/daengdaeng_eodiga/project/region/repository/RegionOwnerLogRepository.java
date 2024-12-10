package com.daengdaeng_eodiga.project.region.repository;

import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.region.dto.RegionOwnerInfo;
import com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog;

public interface RegionOwnerLogRepository  extends JpaRepository<RegionOwnerLog, Integer> {



	@Query(" SELECT rol.id as id, rol.city as city, rol.cityDetail as cityDetail, rol.count as count, rol.user.userId as userId, u.nickname as userNickname, " +
		"        p.id as petId, p.name as petName, p.image as petImage "
		+ "FROM RegionOwnerLog rol " +
		" JOIN ( SELECT r.city AS city , r.cityDetail AS cityDetail , MAX(r.createdAt) AS createdAt " +
		"        FROM RegionOwnerLog r " +
		"        GROUP BY city, cityDetail ) AS ro "
		+ "ON ro.city = rol.city AND ro.cityDetail = rol.cityDetail AND ro.createdAt = rol.createdAt "
		+ "JOIN rol.user u ON rol.user.userId = u.userId " +
		" LEFT JOIN Pet p ON rol.user.userId = p.user.userId")
	List<RegionOwnerInfo> fetchRegionOwner();

}
