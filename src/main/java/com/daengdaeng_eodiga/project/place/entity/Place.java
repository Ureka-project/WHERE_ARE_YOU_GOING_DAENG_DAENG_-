package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.visit.entity.Visit;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "Place")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place extends BaseEntity {
    @Id
    @Column(name = "place_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    private String name;

    private String city;

    @Column(name = "city_detail")
    private String cityDetail;

    private String township;

    private Double latitude;

    private Double longitude;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "street_addresses")
    private String streetAddresses;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "place_type")
    private String placeType;

    private String description;

    @Column(name = "weight_limit")
    private String weightLimit;

    private Boolean parking;

    private Boolean indoor;

    private Boolean outdoor;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlaceScore placeScores = new PlaceScore();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OpeningDate> openingDates = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReviewSummary reviewSummaries = new ReviewSummary();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Favorite> favorite = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlaceMedia placeMedia = new PlaceMedia();
}
