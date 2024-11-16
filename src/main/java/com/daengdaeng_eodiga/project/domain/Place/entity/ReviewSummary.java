package com.daengdaeng_eodiga.project.domain.Place.entity;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "review_summary")
public class ReviewSummary {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "place_id")  // place_id를 외래키로 사용
    @Id
    private Place place;  // place_id를 기본 키로 설정


    @Column(name = "good_summary", length = 255)
    private String goodSummary;

    @Column(name = "bad_summary", length = 255)
    private String badSummary;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    // Getter, Setter
}
