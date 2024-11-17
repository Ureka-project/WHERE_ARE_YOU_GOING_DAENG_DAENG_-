package com.daengdaeng_eodiga.project.review.service;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import com.daengdaeng_eodiga.project.review.ReviewRepository;
import com.daengdaeng_eodiga.project.review.dto.ReviewRegisterRequest;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final UserService userService;
	private final PlaceService placeService;
	private final ReviewRepository reviewRepository;

	public void registerReview(ReviewRegisterRequest request, int userId) {

		User user = userService.findUser(userId);
		Place place = placeService.findPlace(request.placeId());

		Review review = Review.builder()
			.score(request.score())
			.content(request.content())
			.user(user)
			.place(place)
			.visitedAt(request.visitedAt()).build();
		reviewRepository.save(review);
	}

}
