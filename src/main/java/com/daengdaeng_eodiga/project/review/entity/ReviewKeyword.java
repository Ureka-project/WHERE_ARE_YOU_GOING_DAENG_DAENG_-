package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Review_Keyword")
public class ReviewKeyword {

    @EmbeddedId
    private ReviewKeywordId id;

    @MapsId("reviewId")
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Builder
    public ReviewKeyword(Review review, String keyword) {
        this.id = new ReviewKeywordId();
        this.id.setKeyword(keyword);
        this.review = review;
    }


}
