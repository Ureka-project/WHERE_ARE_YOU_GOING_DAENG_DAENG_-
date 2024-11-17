package com.daengdaeng_eodiga.project.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
