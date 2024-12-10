package com.daengdaeng_eodiga.project.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.daengdaeng_eodiga.project.Global.exception.InvalidRequestException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDto {

	private Integer userId;
	private Integer placeId;
	private String placeName;
	private String nickname;
	private String petImg;
	private Integer reviewId;
	private Set<String> pets;
	private String content;
	private Integer score;
	private Set<String> media;
	private Set<String> keywords;
	private LocalDate visitedAt;
	private LocalDateTime createdAt;
	private String reviewType;

	public ReviewDto(Integer userId, Integer placeId, String nickname, String petImg, Integer reviewId, Object pets,
					 String content, Integer score, Object media, Object keywords, LocalDate visitedAt,
					 LocalDateTime createdAt, String placeName, String reviewType) {
		this.userId = userId;
		this.placeId = placeId;
		this.nickname = nickname;
		this.petImg = petImg;
		this.reviewId = reviewId;
		this.pets = convertToSet(pets);
		this.content = content;
		this.score = score;
		this.media = convertToSet(media);
		this.keywords = convertToSet(keywords);
		this.visitedAt = visitedAt;
		this.createdAt = createdAt;
		this.placeName = placeName;
		this.reviewType = reviewType;
	}

	private Set<String> convertToSet(Object input) {
		if (input == null) {
			return new HashSet<>();
		}
		if (input instanceof Set) {
			return (Set<String>)input;
		}
		if (input instanceof List) {
			return new HashSet<>((List<String>) input);
		}
		throw new InvalidRequestException("ReviewDto", input.getClass().getName());
	}
}
