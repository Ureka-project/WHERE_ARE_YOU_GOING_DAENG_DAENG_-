package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Place_Score")
public class PlaceScore extends BaseEntity {

    @Id
    @Column(name = "place_id")
    private int placeId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private Double score = 0.0;

    @Column(name = "review_count")
    private int reviewCount = 0;

    public void updateScore(int score) {
        this.score = (this.score * this.reviewCount + score) / (this.reviewCount + 1);
    }

    @Builder
    public PlaceScore(Place place, Double score, int reviewCount) {
        this.place = place;
        this.placeId = place.getPlaceId();
        this.score = score;
        this.reviewCount = reviewCount;
    }

}
