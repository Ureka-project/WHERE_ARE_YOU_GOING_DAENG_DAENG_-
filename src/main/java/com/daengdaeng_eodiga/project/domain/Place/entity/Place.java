package com.daengdaeng_eodiga.project.domain.Place.entity;

import com.daengdaeng_eodiga.project.domain.Favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.domain.Review.entity.Review;
import com.daengdaeng_eodiga.project.domain.Review.entity.ReviewKeyword;
import com.daengdaeng_eodiga.project.domain.Review.entity.ReviewMedia;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Place")
public class Place {
    @Id
    @Column(name = "place_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "city_detail")
    private String cityDetail;

    @Column(name = "township")
    private String township;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "street_addresses")
    private String streetAddresses;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "url")
    private String url;

    @Column(name = "place_type")
    private String placeType;

    @Column(name = "description")
    private String description;

    @Column(name = "weight_limit")
    private String weightLimit;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "indoor")
    private Boolean indoor;

    @Column(name = "outdoor")
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


    // Getter, Setter
}
