package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;

@Table(name = "place_media")
public class PlaceMedia extends BaseEntity {
    @Id
    @Column(name = "place_media_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false, length = 500)
    private String path;

    @Builder
    public PlaceMedia(Place place, String path) {
        this.place = place;
        this.path = path;
    }
}