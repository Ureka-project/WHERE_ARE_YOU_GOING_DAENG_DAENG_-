package com.daengdaeng_eodiga.project.event.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "start_date")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate  startDate;

    @Column(name = "end_date")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate endDate;

    @Column(name = "place")
    private String place;

    @Column(name = "event_image")
    private String eventImage;
}