package com.daengdaeng_eodiga.project.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daengdaeng_eodiga.project.review.entity.ReviewKeyword;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Integer> {
	void deleteByIdReviewId(int reviewId);

	@Query(value = "SELECT rk.id.keyword FROM Review r LEFT JOIN FETCH ReviewKeyword rk ON rk.id.reviewId = r.reviewId WHERE r.place.placeId = :placeId AND r.user.deletedAt IS NULL GROUP BY rk.id.keyword ORDER BY COUNT(rk.id.keyword) DESC limit 3")
	List<String> findKeywordsByPlaceIdTop3(int placeId);
}
