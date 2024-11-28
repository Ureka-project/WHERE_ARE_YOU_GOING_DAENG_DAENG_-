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
            "NULL AS distance, " +
            "CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite, " +
            "CAST(o.start_time AS CHAR) AS start_time, " +
            "CAST(o.end_time AS CHAR) AS end_time " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "LEFT JOIN opening_date o ON o.place_id = p.place_id " +
            "WHERE p.place_id = :placeId", nativeQuery = true)
    List<Object[]> findPlaceDetailsById(@Param("placeId") int placeId);

    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance, " +
            "EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id AND f.user_id = :userId) AS is_favorite, " +
            "o.start_time AS start_time, o.end_time AS end_time " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "LEFT JOIN opening_date o ON o.place_id = p.place_id " +
            "WHERE " +
            "(:city IS NULL OR p.city LIKE CONCAT('%', :city, '%') " +
            "OR (CASE " +
            "        WHEN :city = '전북' THEN p.city LIKE '%전라북도%' " +
            "        WHEN :city = '경남' THEN p.city LIKE '%경상남도%' " +
            "        WHEN :city = '경북' THEN p.city LIKE '%경상북도%' " +
            "        WHEN :city = '충북' THEN p.city LIKE '%충청북도%' " +
            "        WHEN :city = '충남' THEN p.city LIKE '%충청남도%' " +
            "        WHEN :city = '강원' THEN p.city LIKE '%강원도%' " +
            "        WHEN :city = '제주' THEN p.city LIKE '%제주도%' " +
            "        ELSE 0 " +
            "    END = 1)) " +
            "AND (:cityDetail IS NULL OR p.city_detail LIKE CONCAT('%', :cityDetail, '%')) " +
            "AND (:placeType IS NULL OR c.name = :placeType) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByFiltersAndLocationWithFavorite(@Param("city") String city,
                                                        @Param("cityDetail") String cityDetail,
                                                        @Param("placeType") String placeType,
                                                        @Param("latitude") Double latitude,
                                                        @Param("longitude") Double longitude,
                                                        @Param("userId") int userId);



    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance, " +
            "EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id AND f.user_id = :userId) AS is_favorite, " +
            "o.start_time AS start_time, o.end_time AS end_time " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "LEFT JOIN opening_date o ON o.place_id = p.place_id " +
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


    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, " +
            "p.latitude, p.longitude, p.street_addresses, p.tel_number, p.url, " +
            "c.name AS place_type, p.description, p.parking, p.indoor, p.outdoor, " +
            "NULL AS distance, " +
            "CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite, " +
            "o.start_time, o.end_time, " +
            "COUNT(f.favorite_id) AS favorite_count " +
            "FROM place p " +
            "LEFT JOIN favorite f ON p.place_id = f.place_id " +
            "LEFT JOIN opening_date o ON o.place_id = p.place_id " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "GROUP BY p.place_id, o.start_time, o.end_time " +
            "ORDER BY favorite_count DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTopFavoritePlaces();


    @Query(value = """
            SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
                   p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
                   p.parking, p.indoor, p.outdoor,
                   (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
                   cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance,
                   ps.score AS place_score,
                   o.start_time, o.end_time
            FROM place p
            LEFT JOIN place_score ps ON p.place_id = ps.place_id
            LEFT JOIN common_code c ON p.place_type = c.code_id
            LEFT JOIN opening_date o ON p.place_id = o.place_id
            WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
                   cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= 50
            ORDER BY ps.score DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Object[]> findTopScoredPlacesWithinRadius(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, " +
            "p.latitude, p.longitude, p.street_addresses, p.tel_number, p.url, " +
            "c.name AS place_type, p.description, p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance, " +
            "o.start_time, o.end_time " +
            "FROM place p " +
            "LEFT JOIN opening_date o ON p.place_id = o.place_id " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findNearestPlaces(@Param("latitude") Double latitude,
                                     @Param("longitude") Double longitude);

    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "WHERE (:city IS NULL OR p.city LIKE CONCAT('%', :city, '%')) " +
            "AND (:cityDetail IS NULL OR p.city_detail LIKE CONCAT('%', :cityDetail, '%')) " +
            "AND (:placeType IS NULL OR :placeType = '' OR c.name = :placeType) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByFiltersAndLocation(
            @Param("city") String city,
            @Param("cityDetail") String cityDetail,
            @Param("placeType") String placeType,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude);



    @Query(value = "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
            "p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description, " +
            "p.parking, p.indoor, p.outdoor, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM place p " +
            "LEFT JOIN common_code c ON p.place_type = c.code_id " +
            "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY distance ASC " +
            "LIMIT 30", nativeQuery = true)
    List<Object[]> findByKeywordAndLocation(
            @Param("keyword") String keyword,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude);

}

