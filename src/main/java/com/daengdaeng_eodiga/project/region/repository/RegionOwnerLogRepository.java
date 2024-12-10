package com.daengdaeng_eodiga.project.region.repository;

import com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionOwnerLogRepository extends JpaRepository<RegionOwnerLog, Integer> {

    @Query("SELECT r.city, r.cityDetail " +
        "FROM RegionOwnerLog r " +
        "WHERE r.user.userId = :userId AND r.createdAt = (" +
        "   SELECT MAX(r2.createdAt) " +
        "   FROM RegionOwnerLog r2 " +
        "   WHERE r2.city = r.city AND r2.cityDetail = r.cityDetail " +
        ") " +
        "GROUP BY r.city, r.cityDetail")
    List<Object[]> findCityAndCityDetailByUserId(@Param("userId") Integer userId);

    @Query("SELECT r " +
            "FROM RegionOwnerLog r " +
            "WHERE r.user.userId = :userId " +
            "AND r.city = :city " +
            "AND r.cityDetail = :cityDetail " +
            "AND r.createdAt = (" +
            "   SELECT MAX(r2.createdAt) " +
            "   FROM RegionOwnerLog r2 " +
            "   WHERE r2.city = :city AND r2.cityDetail = :cityDetail )"
            )
    List<Object[]> findByUserIdAndCityAndCityDetail(
            @Param("userId") Integer userId,
            @Param("city") String city,
            @Param("cityDetail") String cityDetail);
}