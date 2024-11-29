package com.daengdaeng_eodiga.project.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.entity.ReviewMedia;

public interface ReviewMediaRepository extends JpaRepository<ReviewMedia, Integer> {
}
