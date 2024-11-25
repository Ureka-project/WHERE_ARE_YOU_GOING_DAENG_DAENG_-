package com.daengdaeng_eodiga.project.place.entity;

import jakarta.persistence.*;
import lombok.Getter;

    @Getter
    @Entity
    @Table(name = "Place_Score")
    public class PlaceScore {

        @Id
        @Column(name = "place_id")
        private int placeId;

        @MapsId("placeId")
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")
        private Place place;

        private Double score;

    }
