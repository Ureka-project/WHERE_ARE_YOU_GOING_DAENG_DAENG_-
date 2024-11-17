package com.daengdaeng_eodiga.project.place.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "review_summary")
public class ReviewSummary {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")
    private Place place;


    @Column(name = "good_summary", length = 255)
    private String goodSummary;

    @Column(name = "bad_summary", length = 255)
    private String badSummary;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;
}
