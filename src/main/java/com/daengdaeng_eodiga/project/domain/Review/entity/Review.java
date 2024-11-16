package com.daengdaeng_eodiga.project.domain.Review.entity;

import com.daengdaeng_eodiga.project.domain.Place.entity.Place;
import jakarta.persistence.*;
import com.daengdaeng_eodiga.project.domain.User.entity.User;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    @Column(name="review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;


    private Integer score;
    private String content;
    private String keywords;

    @Column(name = "visited_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitedAt;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewKeyword> reviewKeywords;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewMedia> reviewMedias = new ArrayList<>();


    // Getter, Setter
}
