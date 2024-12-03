package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
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

}
