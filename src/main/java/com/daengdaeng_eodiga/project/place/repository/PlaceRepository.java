package com.daengdaeng_eodiga.project.place.repository;

import com.daengdaeng_eodiga.project.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance, " +
            "EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id AND f.user_id = :userId) AS is_favorite " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "WHERE (:city IS NULL OR p.city LIKE CONCAT('%', :city, '%')) " +
            "AND (:placeType IS NULL OR c.name = :placeType) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByFiltersAndLocationWithFavorite(@Param("city") String city,
                                                        @Param("placeType") String placeType,
                                                        @Param("latitude") Double latitude,
                                                        @Param("longitude") Double longitude,
                                                        @Param("userId") int userId);

    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance, " +
            "EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id AND f.user_id = :userId) AS is_favorite " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByKeywordAndLocationWithFavorite(@Param("keyword") String keyword,
                                                        @Param("latitude") Double latitude,
                                                        @Param("longitude") Double longitude,
                                                        @Param("userId") int userId);
    @Query(value =
            "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
                    "p.post_code, p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
                    "p.weight_limit, p.parking, p.indoor, p.outdoor, " +
                    "COALESCE(ps.score, 5) AS score, " +
                    "GROUP_CONCAT(rk.keyword) AS keywords, " +
                    "COUNT(DISTINCT  " +
                    "r.review_id) AS review_count " +  // review 카운트를 추가
                    "FROM place p " +
                    "LEFT JOIN review r ON p.place_id = r.place_id " +
                    "LEFT JOIN review_keyword rk ON rk.review_id = r.review_id " +
                    "LEFT JOIN place_score ps ON ps.place_id = p.place_id " +
                    "GROUP BY p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
                    "p.post_code, p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
                    "p.weight_limit, p.parking, p.indoor, p.outdoor",
            nativeQuery = true)
    List<Object[]> findPlaceRecommendationsWithKeywords();

}

