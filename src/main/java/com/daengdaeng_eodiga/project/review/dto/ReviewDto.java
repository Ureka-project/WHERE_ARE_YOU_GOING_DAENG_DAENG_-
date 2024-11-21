package com.daengdaeng_eodiga.project.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDto {

	private Integer userId;
	private Integer placeId;
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

	public ReviewDto(Integer userId, Integer placeId, String nickname, String petImg, Integer reviewId, String pets,
			String content, Integer score, String media, String keywords, LocalDate visitedAt,
			LocalDateTime createdAt) {
		this.userId = userId;
		this.placeId = placeId;
		this.nickname = nickname;
		this.petImg = petImg;
		this.reviewId = reviewId;
		this.pets =  pets!=null?Arrays.stream(pets.split(",")).collect(Collectors.toSet()):null;
		this.content = content;
		this.score = score;
		this.media = media!=null?Arrays.stream(media.split(",")).collect(Collectors.toSet()):null;
		this.keywords = keywords!=null?Arrays.stream(keywords.split(",")).collect(Collectors.toSet()):null;
		this.visitedAt = visitedAt;
		this.createdAt = createdAt;
	}
}
