package com.daengdaeng_eodiga.project.domain.Place.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Place_Score")
public class PlaceScore {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")  // place_id를 외래키로 사용
    @Id
    private Place place;  // place_id를 기본 키로 설정

    private Double score;

    // Getter, Setter
}
