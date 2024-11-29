package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@Embeddable
@NoArgsConstructor
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
        return keyword != null ? keyword.equals(that.keyword) : that.keyword == null;
    }

    @Override
    public int hashCode() {
        int result = keyword != null ? keyword.hashCode() : 0;
        result = 31 * result + Integer.hashCode(reviewId);
        return result;
    }
}
