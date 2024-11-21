package com.daengdaeng_eodiga.project.review.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.entity.ReviewKeyword;
import com.daengdaeng_eodiga.project.review.repository.ReviewKeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewKeywordsService {
	private final ReviewKeywordRepository reviewKeywordRepository;

	public void saveReviewKeywords(Review review, Set<String> keywords) {
		List<ReviewKeyword> keywordsEntity = keywords.stream()
			.map(keyword -> ReviewKeyword.builder().review(review).keyword(keyword).build())
			.toList();
		reviewKeywordRepository.saveAll(keywordsEntity);
	}

}