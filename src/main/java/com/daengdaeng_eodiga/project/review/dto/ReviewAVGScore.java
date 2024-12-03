package com.daengdaeng_eodiga.project.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReviewAVGScore {
	private int placeId;
	private double avgScore;
	private int reviewCount;

}
