package com.daengdaeng_eodiga.project.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "review_summary")
public class ReviewSummary {

    @Id
    @Column(name = "place_id", nullable = false)
    private Integer placeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "good_summary", length = 255)
    private String goodSummary;

    @Column(name = "bad_summary", length = 255)
    private String badSummary;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public ReviewSummary() {}

    public ReviewSummary(Place place, String goodSummary, String badSummary, LocalDateTime updateDate) {
        this.place = place;
        this.goodSummary = goodSummary;
        this.badSummary = badSummary;
        this.updateDate = updateDate;
    }

}
