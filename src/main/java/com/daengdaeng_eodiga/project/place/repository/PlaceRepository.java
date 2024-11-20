package com.daengdaeng_eodiga.project.place.repository;

import com.daengdaeng_eodiga.project.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    // 필터링 검색 메서드
    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "WHERE (:city IS NULL OR p.city LIKE CONCAT('%', :city, '%')) " +
            "AND (:placeType IS NULL OR c.name = :placeType) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByFiltersAndLocationWithCode(
            @Param("city") String city,
            @Param("placeType") String placeType,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude);

    // 키워드 검색 메서드
    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM place p " +
            "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByKeywordAndLocation(
            @Param("keyword") String keyword,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude);
}
