package com.daengdaeng_eodiga.project.review.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.pet.entity.QPet;
import com.daengdaeng_eodiga.project.place.entity.QPlace;
import com.daengdaeng_eodiga.project.review.entity.QReview;
import com.daengdaeng_eodiga.project.review.entity.QReviewKeyword;
import com.daengdaeng_eodiga.project.review.entity.QReviewMedia;
import com.daengdaeng_eodiga.project.review.entity.QReviewPet;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.user.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	/**
	 *
	 * OrderType 에 따라 특정 Place의 리뷰 목록을 정렬해서 조회한다.
	 * No-offset으로 구현
	 *
	 * @auther : 김가은
	 * @return : List<Review>
	 */


	@Override
	public List<Review> findAllByPlace(Integer placeId, OrderType orderType, int lastReviewId, int lastScore,int size) {
		QReview r = QReview.review;
		QUser u = QUser.user;
		QReviewPet rp = QReviewPet.reviewPet;
		QPet p = QPet.pet;
		QReviewKeyword rk = QReviewKeyword.reviewKeyword;
		QReviewMedia rm = QReviewMedia.reviewMedia;
		QPlace pl = QPlace.place;

		return queryFactory
			.select( r)
			.from(r)
			.leftJoin(r.user, u).fetchJoin()
			.leftJoin(r.reviewPets, rp).fetchJoin()
			.leftJoin(rp.pet, p).fetchJoin()
			.leftJoin(r.reviewKeywords, rk)
			.leftJoin(r.reviewMedias, rm)
			.leftJoin(r.place, pl).fetchJoin()
			.leftJoin(pl.placeScores).fetchJoin()
			.where(pl.placeId.eq(placeId),getNonOffsetCondition(orderType, r, lastReviewId, lastScore))
			.orderBy(getOrderSpecifier(orderType, r))
			.limit(size)
			.fetch();
	}

	/**
	 *
	 * OrderType 에 따라 정렬 조건을 반환한다.
	 *
	 * @auther : 김가은
	 * @return : OrderSpecifier
	 */

	private OrderSpecifier getOrderSpecifier(OrderType orderType, QReview r) {

		if(orderType == OrderType.LATEST){
			return  new OrderSpecifier<>(Order.DESC, r.reviewId);
		} else if (orderType == OrderType.HIGH_SCORE) {
			return new OrderSpecifier<>(Order.DESC, r.score);
		} else {
			return new OrderSpecifier<>(Order.ASC, r.score);
		}

	}
	/**
	 *
	 * 처음 조회한 경우, lastReviewId = 0 lastScore = -1 이다.
	 *
	 * @auther : 김가은
	 * @return : BooleanExpression
	 */

	private BooleanExpression getNonOffsetCondition(OrderType orderType, QReview r, int lastReviewId, int lastScore) {

		if(lastReviewId == 0 || lastScore == -1){
			return null;
		}

		if(orderType == OrderType.LATEST){
			return  r.reviewId.lt(lastReviewId);
		} else if (orderType == OrderType.HIGH_SCORE) {
			return r.score.lt(lastScore).or(r.score.eq(lastScore).and(r.reviewId.lt(lastReviewId)));
		} else {
			return r.score.gt(lastScore).or(r.score.eq(lastScore).and(r.reviewId.lt(lastReviewId)));
		}

	}
}