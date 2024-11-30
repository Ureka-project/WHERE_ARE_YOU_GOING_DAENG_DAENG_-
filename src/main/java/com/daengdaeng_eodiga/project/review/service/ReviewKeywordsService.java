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

	public List<ReviewKeyword> saveReviewKeywords(Review review, List<String> keywords) {
		List<ReviewKeyword> keywordsEntity = keywords.stream()
			.map(keyword -> ReviewKeyword.builder().review(review).keyword(keyword).build())
			.toList();
		return reviewKeywordRepository.saveAll(keywordsEntity);
	}

	public List<String> fetchBestReviewKeywordsTop3(int placeId) {
		return reviewKeywordRepository.findKeywordsByPlaceIdTop3(placeId);
	}

}
