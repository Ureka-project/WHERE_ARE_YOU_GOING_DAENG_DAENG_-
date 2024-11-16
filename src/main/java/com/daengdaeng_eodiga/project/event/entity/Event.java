package com.daengdaeng_eodiga.project.event.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "place")
    private String place;

    @Column(name = "event_image")
    private String eventImage;

}
