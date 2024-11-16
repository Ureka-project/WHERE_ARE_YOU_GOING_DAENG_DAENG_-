package com.daengdaeng_eodiga.project.domain.Review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class ReviewKeywordId implements Serializable {

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "review_id")
    private int reviewId;  // reviewId를 int로 변경

    // 기본 생성자
    public ReviewKeywordId() {
    }

    // 생성자
    public ReviewKeywordId(String keyword, int reviewId) {
        this.keyword = keyword;
        this.reviewId = reviewId;
    }

    // equals()와 hashCode() 구현 (복합 키에 필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewKeywordId that = (ReviewKeywordId) o;

        if (reviewId != that.reviewId) return false;  // reviewId를 int로 비교
        return keyword.equals(that.keyword);
    }

    @Override
    public int hashCode() {
        int result = keyword.hashCode();
        result = 31 * result + Integer.hashCode(reviewId);  // reviewId를 int로 처리
        return result;
    }

    // Getter, Setter
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
}
