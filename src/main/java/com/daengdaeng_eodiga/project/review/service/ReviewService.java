package com.daengdaeng_eodiga.project.review.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;

import com.daengdaeng_eodiga.project.region.service.RegionService;
import com.daengdaeng_eodiga.project.review.dto.ReviewDto;
import com.daengdaeng_eodiga.project.review.dto.ReviewsResponse;
import com.daengdaeng_eodiga.project.review.entity.ReviewKeyword;
import com.daengdaeng_eodiga.project.review.entity.ReviewMedia;
import com.daengdaeng_eodiga.project.review.entity.ReviewPet;
import com.daengdaeng_eodiga.project.review.repository.ReviewMediaRepository;
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
	private final ReviewMediaRepository reviewMediaRepository;
	private final CommonCodeService commonCodeService;
	private final RegionService regionService;

	public ReviewDto registerReview(ReviewRegisterRequest request, int userId) {

		User user = userService.findUser(userId);
		Place place = placeService.findPlace(request.placeId());
		List<Pet> pets = petService.confirmUserPet(user, request.pets());

		Review review = createAndSaveReview(request, user, place);

		List<ReviewPet> savedReviewPets = saveReviewPetsIfPresent(review, pets);
		List<ReviewKeyword> savedReviewKeywords = saveReviewKeywordsIfPresent(review, request.keywords().stream().toList());
		List<ReviewMedia> savedReviewMedia = saveReviewMediaIfPresent(review, request.media());
		addCountVisitRegion(userId, place, review);
		return createReviewDto(review, savedReviewPets, savedReviewKeywords, savedReviewMedia);
	}

	private void addCountVisitRegion(int userId, Place place, Review review) {
		if(review.getReviewtype().equals("REVIEW_TYP_02")){
			regionService.addCountVisitRegion(place.getCity(), place.getCityDetail(), userId);
		}
	}

	private Review createAndSaveReview(ReviewRegisterRequest request, User user, Place place) {
		commonCodeService.isCommonCode(request.reviewType());
		Review review = Review.builder()
				.score(request.score())
				.content(request.content())
				.user(user)
				.place(place)
				.visitedAt(request.visitedAt())
				.reviewtype(request.reviewType())
				.build();

		return reviewRepository.save(review);
	}

	private List<ReviewPet> saveReviewPetsIfPresent(Review review, List<Pet> pets) {
		return pets == null || pets.isEmpty() ? List.of() : reviewPetService.saveReviewPet(review, pets);
	}

	private List<ReviewKeyword> saveReviewKeywordsIfPresent(Review review, List<String> keywords) {
		return keywords == null || keywords.isEmpty()? List.of() : reviewKeywordsService.saveReviewKeywords(review, keywords);
	}

	private List<ReviewMedia> saveReviewMediaIfPresent(Review review, List<String> mediaPaths) {
		if (mediaPaths==null || mediaPaths.isEmpty()) {
			return List.of();
		}
		List<ReviewMedia> reviewMedia = mediaPaths.stream()
			.map(path -> ReviewMedia.builder().review(review).path(path).build())
			.toList();
		return reviewMediaRepository.saveAll(reviewMedia);
	}

	private ReviewDto createReviewDto(Review review, List<ReviewPet> savedReviewPets,
		List<ReviewKeyword> savedReviewKeywords, List<ReviewMedia> savedReviewMedia) {
		return new ReviewDto(
			review.getUser().getUserId(),
			review.getPlace().getPlaceId(),
			review.getUser().getNickname(),
			savedReviewPets.isEmpty() ? null : savedReviewPets.get(0).getPet().getImage(),
			review.getReviewId(),
			savedReviewPets.stream().map(rp -> rp.getPet().getName()).toList(),
			review.getContent(),
			review.getScore(),
			savedReviewMedia.stream().map(ReviewMedia::getPath).toList(),
			savedReviewKeywords.stream().map(keyword ->
				commonCodeService.getCommonCodeName(keyword.getId().getKeyword())
			).toList(),
			review.getVisitedAt(),
			review.getCreatedAt(),
			review.getPlace().getName(),
			commonCodeService.getCommonCodeName(review.getReviewtype())

		);
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
		ReviewsResponse response = new ReviewsResponse(reviews,reviewsPage.getTotalElements(),reviewsPage.getNumber(),reviewsPage.getSize(),reviewsPage.isFirst(),reviewsPage.isLast(),orderType,score,keywords.stream().map(keyword -> commonCodeService.getCommonCodeName(keyword)).collect(Collectors.toList()));

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

	private List<ReviewDto> getReviewDto(Page<Object[]> reviewsPage) {
		List<ReviewDto> reviews = new ArrayList<>();
		for (Object[] result : reviewsPage.getContent()) {

			java.sql.Date sqlDate = (java.sql.Date) result[10];
			LocalDate visitedAt = sqlDate != null ? sqlDate.toLocalDate() : null;

			java.sql.Timestamp timestamp = (java.sql.Timestamp) result[11];
			LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

			List<String> pets = getStrings((String)result[5]);
			List<String> media = getStrings((String)result[8]);
			List<String> keywords = getStrings((String)result[9]).stream().map(keyword -> commonCodeService.getCommonCodeName(keyword) ).collect(Collectors.toList());

			ReviewDto reviewDto = new ReviewDto(
				(Integer) result[0],
				(Integer) result[1],
				(String) result[2],
				(String) result[3],
				(Integer) result[4],
				pets,
				(String) result[6],
				(Integer) result[7],
				media,
				keywords,
				visitedAt,
				createdAt,
				(String) result[12],
				(String) result[13]

			);
			reviews.add(reviewDto);
		}
		return reviews;
	}

	private static List<String> getStrings(String str) {
		List<String> pets;
		if(str != null){
			pets = Arrays.stream((str).split(","))
				.collect(Collectors.toList());
		}else {
			pets = Collections.emptyList();
		}
		return pets;
	}

}
