package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Review_Media")
public class ReviewMedia {
    @Id
    @Column(name = "review_media_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false, length = 500)
    private String path;

    @Builder
    public ReviewMedia(Review review, String path) {
        this.review = review;
        this.path = path;
    }

}
