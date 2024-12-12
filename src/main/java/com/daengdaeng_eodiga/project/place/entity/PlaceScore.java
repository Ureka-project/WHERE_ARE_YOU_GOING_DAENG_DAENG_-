package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Place_Score")
public class PlaceScore extends BaseEntity {

    @Id
    @Column(name = "place_id")
    private int placeId;

    @MapsId("placeId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")
    private Place place;

    private Double score;

    @Column(name = "review_count")
    private int reviewCount;

    public void updateScore(int score) {
        this.score = (this.score * this.reviewCount + score) / (this.reviewCount + 1);
    }

    @Builder
    public PlaceScore(Place place, Double score, int reviewCount) {
        this.place = place;
        this.score = score;
        this.reviewCount = reviewCount;
    }

}
