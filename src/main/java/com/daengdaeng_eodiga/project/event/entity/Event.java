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

    @Column(name = "event_image", length = 700)
    private String eventImage;

    @Column(name = "event_description", length = 500)
    private String eventDescription;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_address")
    private String placeAddress;

    @Column(name = "start_date")
    private LocalDate  startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}