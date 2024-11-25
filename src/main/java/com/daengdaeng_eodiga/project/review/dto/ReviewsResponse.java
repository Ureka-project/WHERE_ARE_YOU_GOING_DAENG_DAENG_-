package com.daengdaeng_eodiga.project.review.dto;

import java.util.List;

import com.daengdaeng_eodiga.project.Global.enums.OrderType;

public record ReviewsResponse(List<ReviewDto> reviews, long total, int page, int size, boolean isFirst, boolean isLast, OrderType sortedType, String score, List<String> bestKeywords) {

}
