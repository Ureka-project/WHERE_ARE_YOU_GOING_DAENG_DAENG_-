package com.daengdaeng_eodiga.project.place.repository;

import com.daengdaeng_eodiga.project.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       NULL AS distance,
       CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite,
       CAST(o.start_time AS CHAR) AS start_time,
       CAST(o.end_time AS CHAR) AS end_time,
       (SELECT COUNT(*) FROM favorite f WHERE f.place_id = p.place_id) AS favorite_count,
       ps.score AS place_score,
       pm.path AS imageurl
FROM place p
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN opening_date o ON o.place_id = p.place_id
LEFT JOIN place_score ps ON p.place_id = ps.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id
WHERE p.place_id = :placeId;
""", nativeQuery = true)
    List<Object[]> findPlaceDetailsById(@Param("placeId") int placeId);




    @Query(value =
            "SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
                    "p.post_code, p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
                    "p.weight_limit, p.parking, p.indoor, p.outdoor, " +
                    "COALESCE(ps.score, 2) AS score, " +
                    "GROUP_CONCAT(DISTINCT rk.keyword) AS keywords, " +
                    "COUNT(DISTINCT r.review_id) AS review_count, " +
                    "GROUP_CONCAT(COALESCE(pm.path, '')) AS imageurl " +
                    "FROM place p " +
                    "LEFT JOIN review r ON p.place_id = r.place_id " +
                    "LEFT JOIN review_keyword rk ON rk.review_id = r.review_id " +
                    "LEFT JOIN place_score ps ON ps.place_id = p.place_id " +
                    "LEFT JOIN place_media pm ON pm.place_id = p.place_id " +
                    "GROUP BY p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude, " +
                    "p.post_code, p.street_addresses, p.tel_number, p.url, p.place_type, p.description, " +
                    "p.weight_limit, p.parking, p.indoor, p.outdoor",
            nativeQuery = true)
    List<Object[]> findPlaceRecommendationsWithKeywords();


    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       NULL AS distance,
       CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite,
       o.start_time, o.end_time,
       COUNT(f.favorite_id) AS favorite_count,
       ps.score AS place_score,
       GROUP_CONCAT(pm.path) AS imageurl
FROM place p
LEFT JOIN favorite f ON p.place_id = f.place_id
LEFT JOIN opening_date o ON o.place_id = p.place_id
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN place_score ps ON p.place_id = ps.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id
GROUP BY p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
         p.street_addresses, p.tel_number, p.url, c.name, p.description,
         p.parking, p.indoor, p.outdoor, o.start_time, o.end_time, ps.score
ORDER BY favorite_count DESC
LIMIT 3;
""", nativeQuery = true)
    List<Object[]> findTopFavoritePlaces();




    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
       cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance,
       CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite,
       o.start_time, o.end_time,
       (SELECT COUNT(*) FROM favorite f WHERE f.place_id = p.place_id) AS favorite_count,
       ps.score AS place_score,
       pm.path AS imageurl
FROM place p
LEFT JOIN place_score ps ON p.place_id = ps.place_id
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN opening_date o ON p.place_id = o.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id
WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
       cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= 50
ORDER BY ps.score DESC
LIMIT 3;
""", nativeQuery = true)
    List<Object[]> findTopScoredPlacesWithinRadius(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
       cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance,
       CASE WHEN EXISTS (SELECT 1 FROM favorite f WHERE f.place_id = p.place_id) THEN 1 ELSE 0 END AS is_favorite,
       o.start_time, o.end_time,
       (SELECT COUNT(*) FROM favorite f WHERE f.place_id = p.place_id) AS favorite_count,
       ps.score AS place_score,
       pm.path AS imageurl
FROM place p
LEFT JOIN opening_date o ON p.place_id = o.place_id
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN place_score ps ON p.place_id = ps.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id
ORDER BY distance ASC
LIMIT 30;
""", nativeQuery = true)
    List<Object[]> findNearestPlaces(@Param("latitude") Double latitude, @Param("longitude") Double longitude);


    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
       cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance,
       CASE WHEN :userId = -1 THEN 0
            WHEN f.favorite_id IS NOT NULL THEN 1
            ELSE 0
       END AS is_favorite,
       CAST(o.start_time AS CHAR) AS start_time,
       CAST(o.end_time AS CHAR) AS end_time,
       (SELECT COUNT(f2.favorite_id) FROM favorite f2 WHERE f2.place_id = p.place_id) AS favorite_count,
       ps.score AS place_score,
       pm.path AS imageurl -- 추가
FROM place p
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN favorite f ON p.place_id = f.place_id AND f.user_id = :userId
LEFT JOIN opening_date o ON o.place_id = p.place_id
LEFT JOIN place_score ps ON ps.place_id = p.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id -- 추가
WHERE (:city IS NULL OR p.city LIKE CONCAT('%', :city, '%'))
  AND (:cityDetail IS NULL OR p.city_detail LIKE CONCAT('%', :cityDetail, '%'))
  AND (:placeType IS NULL OR :placeType = '' OR c.code_id = :placeType)
ORDER BY distance ASC
LIMIT 30;
""", nativeQuery = true)
    List<Object[]> findByFiltersAndLocation(
            @Param("city") String city,
            @Param("cityDetail") String cityDetail,
            @Param("placeType") String placeType,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("userId") Integer userId
    );

    @Query(value = """
SELECT p.place_id, p.name, p.city, p.city_detail, p.township, p.latitude, p.longitude,
       p.street_addresses, p.tel_number, p.url, c.name AS place_type, p.description,
       p.parking, p.indoor, p.outdoor,
       (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) *
       cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance,
       CASE WHEN :userId = -1 THEN 0
            WHEN f.favorite_id IS NOT NULL THEN 1
            ELSE 0
       END AS is_favorite,
       CAST(o.start_time AS CHAR) AS start_time,
       CAST(o.end_time AS CHAR) AS end_time,
       (SELECT COUNT(f2.favorite_id) FROM favorite f2 WHERE f2.place_id = p.place_id) AS favorite_count,
       ps.score AS place_score,
       pm.path AS imageurl
FROM place p
LEFT JOIN common_code c ON p.place_type = c.code_id
LEFT JOIN favorite f ON p.place_id = f.place_id AND f.user_id = :userId
LEFT JOIN opening_date o ON o.place_id = p.place_id
LEFT JOIN place_score ps ON ps.place_id = p.place_id
LEFT JOIN place_media pm ON pm.place_id = p.place_id
WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
ORDER BY distance ASC
LIMIT 30;
""", nativeQuery = true)
    List<Object[]> findByKeywordAndLocation(
            @Param("keyword") String keyword,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("userId") Integer userId
    );



    Optional<Place> findByPlaceId(Integer placeId);



    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Favorite f WHERE f.place.placeId = :placeId AND f.user.userId = :userId")
    boolean existsFavoriteByPlaceIdAndUserId(@Param("placeId") int placeId, @Param("userId") int userId);

}


