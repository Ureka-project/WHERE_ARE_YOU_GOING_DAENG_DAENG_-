package com.daengdaeng_eodiga.project.review.repository;

import java.util.List;

import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.review.entity.Review;

public interface ReviewRepositoryCustom {

	List<Review> findAllByPlace(Integer placeId, OrderType orderType,int lastReviewId, int lastScore, int size);
}
