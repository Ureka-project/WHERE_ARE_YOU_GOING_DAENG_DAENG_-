package com.daengdaeng_eodiga.project.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.review.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query(value = "SELECT "
		+ "         u.user_id AS userId, r.place_id, u.nickname, "
		+ "			SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT p.image ORDER BY p.name ASC), ',', 1) AS petImg, r.review_id, "
		+ "        GROUP_CONCAT(DISTINCT p.name ORDER BY p.name ASC) AS pets, "
		+ "			r.content, r.score, "
		+ "        GROUP_CONCAT(DISTINCT rm.path ORDER BY rm.path ASC) AS media, "
		+ "        GROUP_CONCAT(DISTINCT rk.keyword ORDER BY rk.keyword ASC) AS keywords, "
		+ "        r.visited_at AS visitedAt, r.created_at AS createdAt  "
		+ "    FROM review r "
		+ "    LEFT JOIN users u ON r.user_id = u.user_id "
		+ "    LEFT JOIN review_pet rp ON rp.review_id = r.review_id "
		+ "    LEFT JOIN pet p ON rp.pet_id = p.pet_id "
		+ "    LEFT JOIN review_keyword rk ON rk.review_id = r.review_id "
		+ "	   LEFT JOIN review_media rm ON rm.review_id = r.review_id "
		+ "    WHERE r.place_id = :placeId "
		+ "    GROUP BY r.place_id, u.user_id, r.review_id, r.content,r.score,r.visited_at,r.created_at"
		+ "	   ORDER BY createdAt desc ; ", nativeQuery = true)
	Page<Object[]> findAllByPlaceOrderByLatest(int placeId, Pageable pageable);

	@Query(value = "SELECT "
		+ "         u.user_id AS userId, r.place_id, u.nickname, "
		+ "			SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT p.image ORDER BY p.name ASC), ',', 1) AS petImg, r.review_id, "
		+ "        GROUP_CONCAT(DISTINCT p.name ORDER BY p.name ASC) AS pets, "
		+ "			r.content, r.score, "
		+ "        GROUP_CONCAT(DISTINCT rm.path ORDER BY rm.path ASC) AS media, "
		+ "        GROUP_CONCAT(DISTINCT rk.keyword ORDER BY rk.keyword ASC) AS keywords, "
		+ "        r.visited_at AS visitedAt, r.created_at AS createdAt  "
		+ "    FROM review r "
		+ "    LEFT JOIN users u ON r.user_id = u.user_id "
		+ "    LEFT JOIN review_pet rp ON rp.review_id = r.review_id "
		+ "    LEFT JOIN pet p ON rp.pet_id = p.pet_id "
		+ "    LEFT JOIN review_keyword rk ON rk.review_id = r.review_id "
		+ "	   LEFT JOIN review_media rm ON rm.review_id = r.review_id "
		+ "    WHERE r.place_id = :placeId "
		+ "    GROUP BY r.place_id, u.user_id, r.review_id, r.content,r.score,r.visited_at,r.created_at "
		+ "	   ORDER BY r.score desc ; ", nativeQuery = true)
	Page<Object[]> findAllByPlaceOrderByHighScore(int placeId, Pageable pageable);

	@Query(value = "SELECT "
		+ "         u.user_id AS userId, r.place_id, u.nickname, "
		+ "			SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT p.image ORDER BY p.name ASC), ',', 1) AS petImg, r.review_id, "
		+ "        GROUP_CONCAT(DISTINCT p.name ORDER BY p.name ASC) AS pets, "
		+ "			r.content, r.score, "
		+ "        GROUP_CONCAT(DISTINCT rm.path ORDER BY rm.path ASC) AS media, "
		+ "        GROUP_CONCAT(DISTINCT rk.keyword ORDER BY rk.keyword ASC) AS keywords, "
		+ "        r.visited_at AS visitedAt, r.created_at AS createdAt  "
		+ "    FROM review r "
		+ "    LEFT JOIN users u ON r.user_id = u.user_id "
		+ "    LEFT JOIN review_pet rp ON rp.review_id = r.review_id "
		+ "    LEFT JOIN pet p ON rp.pet_id = p.pet_id "
		+ "    LEFT JOIN review_keyword rk ON rk.review_id = r.review_id "
		+ "	   LEFT JOIN review_media rm ON rm.review_id = r.review_id "
		+ "    WHERE r.place_id = :placeId "
		+ "    GROUP BY r.place_id, u.user_id, r.review_id, r.content,r.score,r.visited_at,r.created_at "
		+ "	   ORDER BY r.score; ", nativeQuery = true)
	Page<Object[]> findAllByPlaceOrderByLowScore(int placeId, Pageable pageable);

	@Query(value = "SELECT "
		+ "         u.user_id AS userId, r.place_id, u.nickname, "
		+ "			SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT p.image ORDER BY p.name ASC), ',', 1) AS petImg, r.review_id, "
		+ "        GROUP_CONCAT(DISTINCT p.name ORDER BY p.name ASC) AS pets, "
		+ "			r.content, r.score, "
		+ "        GROUP_CONCAT(DISTINCT rm.path ORDER BY rm.path ASC) AS media, "
		+ "        GROUP_CONCAT(DISTINCT rk.keyword ORDER BY rk.keyword ASC) AS keywords, "
		+ "        r.visited_at AS visitedAt, r.created_at AS createdAt  "
		+ "    FROM review r "
		+ "    LEFT JOIN users u ON r.user_id = u.user_id "
		+ "    LEFT JOIN review_pet rp ON rp.review_id = r.review_id "
		+ "    LEFT JOIN pet p ON rp.pet_id = p.pet_id "
		+ "    LEFT JOIN review_keyword rk ON rk.review_id = r.review_id "
		+ "	   LEFT JOIN review_media rm ON rm.review_id = r.review_id "
		+ "    WHERE r.user_id = :userId "
		+ "    GROUP BY r.place_id, u.user_id, r.review_id, r.content,r.score,r.visited_at,r.created_at"
		+ "	   ORDER BY r.created_at desc; ", nativeQuery = true)
	Page<Object[]> findAllByUserOrderByLatest(int userId, Pageable pageable);
}
