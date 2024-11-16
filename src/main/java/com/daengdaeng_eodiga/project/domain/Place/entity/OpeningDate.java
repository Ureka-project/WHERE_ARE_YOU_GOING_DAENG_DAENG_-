package com.daengdaeng_eodiga.project.domain.Place.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "opening_date")
public class OpeningDate {
    @Id
    @Column(name="opening_date_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int openingDateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "Field")
    private String field;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
    // Getter, Setter
}
