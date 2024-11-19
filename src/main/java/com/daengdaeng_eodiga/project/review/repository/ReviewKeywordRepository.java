package com.daengdaeng_eodiga.project.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.review.entity.ReviewKeyword;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Integer> {
	void deleteByIdReviewId(int reviewId);
}
