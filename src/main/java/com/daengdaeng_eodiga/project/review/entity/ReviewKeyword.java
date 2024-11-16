package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Review_Keyword")
public class ReviewKeyword {

    @EmbeddedId
    private ReviewKeywordId id;

    @MapsId("reviewId")
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;


}
