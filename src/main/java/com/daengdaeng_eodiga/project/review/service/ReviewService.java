package com.daengdaeng_eodiga.project.review.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;

import com.daengdaeng_eodiga.project.review.dto.ReviewDto;
import com.daengdaeng_eodiga.project.review.dto.ReviewsResponse;
import com.daengdaeng_eodiga.project.review.repository.ReviewRepository;
import com.daengdaeng_eodiga.project.review.dto.ReviewRegisterRequest;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

	private final UserService userService;
	private final PlaceService placeService;
	private final ReviewRepository reviewRepository;
	private final ReviewKeywordsService reviewKeywordsService;
	private final PetService petService;
	private final ReviewPetService reviewPetService;



	public void registerReview(ReviewRegisterRequest request, int userId) {
		User user = userService.findUser(userId);
		Place place = placeService.findPlace(request.placeId());
		List<Pet> pets = petService.confirmUserPet(user, request.pets());

		Review review = Review.builder()
			.score(request.score())
			.content(request.content())
			.user(user)
			.place(place)
			.visitedAt(request.visitedAt()).build();
		reviewRepository.save(review);
		reviewKeywordsService.saveReviewKeywords(review, request.keywords());
		reviewPetService.saveReviewPet(review, pets);
	}

	public void deleteReview(int reviewId) {
		reviewRepository.deleteById(reviewId);
	}

	public ReviewsResponse fetchPlaceReviews(int placeId, int page, int size, OrderType orderType) {
		Place place = placeService.findPlace(placeId);
		Double scoreDouble = placeService.findPlaceScore(placeId);

		DecimalFormat df = new DecimalFormat("#.##");
		String score = df.format(scoreDouble);

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> reviewsPage;
		if(orderType == OrderType.LATEST){
			reviewsPage = reviewRepository.findAllByPlaceOrderByLatest(place.getPlaceId(), pageable);
		} else if (orderType == OrderType.HIGH_SCORE) {
			reviewsPage = reviewRepository.findAllByPlaceOrderByHighScore(place.getPlaceId(), pageable);
		} else {
			reviewsPage = reviewRepository.findAllByPlaceOrderByLowScore(place.getPlaceId(), pageable);
		}
		List<ReviewDto> reviews = getReviewDto(reviewsPage);
		List<String> keywords = reviewKeywordsService.fetchBestReviewKeywordsTop3(placeId);
		ReviewsResponse response = new ReviewsResponse(reviews,reviewsPage.getTotalElements(),reviewsPage.getNumber(),reviewsPage.getSize(),reviewsPage.isFirst(),reviewsPage.isLast(),orderType,score,keywords);

		return response;
	}

	public ReviewsResponse fetchUserReviews(int userId,int page, int size) {
		User user = userService.findUser(userId);
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> reviewsPage = reviewRepository.findAllByUserOrderByLatest(user.getUserId(),pageable);
		List<ReviewDto> reviews = getReviewDto(reviewsPage);
		ReviewsResponse response = new ReviewsResponse(reviews,reviewsPage.getTotalElements(),reviewsPage.getNumber(),reviewsPage.getSize(),reviewsPage.isFirst(),reviewsPage.isLast(),OrderType.LATEST,null,null);
		return response;
	}

	private static List<ReviewDto> getReviewDto(Page<Object[]> reviewsPage) {
		List<ReviewDto> reviews = new ArrayList<>();
		for (Object[] result : reviewsPage.getContent()) {

			java.sql.Date sqlDate = (java.sql.Date) result[10];
			LocalDate visitedAt = sqlDate != null ? sqlDate.toLocalDate() : null;

			java.sql.Timestamp timestamp = (java.sql.Timestamp) result[11];
			LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

			ReviewDto reviewDto = new ReviewDto(
				(Integer) result[0],
				(Integer) result[1],
				(String) result[2],
				(String) result[3],
				(Integer) result[4],
				(String) result[5],
				(String) result[6],
				(Integer) result[7],
				(String) result[8],
				(String) result[9],
				visitedAt,
				createdAt
			);
			reviews.add(reviewDto);
		}
		return reviews;
	}

}
