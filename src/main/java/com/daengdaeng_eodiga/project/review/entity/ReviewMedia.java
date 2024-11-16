package com.daengdaeng_eodiga.project.review.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Review_Media")
public class ReviewMedia {
    @Id
    @Column(name = "review_media_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private String path;

}
