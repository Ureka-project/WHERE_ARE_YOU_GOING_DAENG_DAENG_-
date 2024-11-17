package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class ReviewKeywordId implements Serializable {

    private String keyword;

    @Column(name = "review_id")
    private int reviewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewKeywordId that = (ReviewKeywordId) o;

        if (reviewId != that.reviewId) return false;
        return keyword.equals(that.keyword);
    }

    @Override
    public int hashCode() {
        int result = keyword.hashCode();
        result = 31 * result + Integer.hashCode(reviewId);  // reviewId를 int로 처리
        return result;
    }

}
