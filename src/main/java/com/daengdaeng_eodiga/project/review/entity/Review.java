package com.daengdaeng_eodiga.project.review.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.place.entity.Place;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.daengdaeng_eodiga.project.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "Review")
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @Column(name="review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private int score;

    private String content;

    @Column(name = "visited_at")
    private LocalDate visitedAt;

    @Column(name = "review_type")
    private String reviewtype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ReviewKeyword> reviewKeywords=new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ReviewMedia> reviewMedias = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ReviewPet> reviewPets = new ArrayList<>();

    @Builder
    public Review(int score, String content, LocalDate visitedAt, Place place, User user, String reviewtype) {
        this.score = score;
        this.content = content;
        this.visitedAt = visitedAt;
        this.place = place;
        this.user = user;
        this.reviewtype = reviewtype;
    }
}
