package com.daengdaeng_eodiga.project.place.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "opening_date")
public class OpeningDate {
    @Id
    @Column(name="opening_date_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int openingDateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "day_type")
    private String dayType;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Builder
    public OpeningDate(Place place, String dayType, String startTime, String endTime) {
        this.place = place;
        this.dayType = dayType;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public OpeningDate() {}
}
