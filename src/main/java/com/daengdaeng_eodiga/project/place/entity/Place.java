package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
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
    private List<Visited> visitedPlaces = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlaceScore placeScores = new PlaceScore();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningDate> openingDates = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReviewSummary reviewSummaries = new ReviewSummary();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorite = new ArrayList<>();
}
