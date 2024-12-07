package com.daengdaeng_eodiga.project.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.daengdaeng_eodiga.project.place.entity.ReviewSummary;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Integer> {
}
