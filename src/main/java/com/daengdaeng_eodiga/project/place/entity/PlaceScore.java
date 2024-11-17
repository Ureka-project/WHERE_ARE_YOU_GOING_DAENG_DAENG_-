package com.daengdaeng_eodiga.project.place.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Place_Score")
public class PlaceScore {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")
    private Place place;

    private Double score;

}
