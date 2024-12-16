package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.visit.entity.Visit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"placeScores"})
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

    @Column(name = "thumb_img_path",length = 700)
    private String thumbImgPath;

    @OneToMany(mappedBy = "place", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Visit> visits = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.PERSIST, orphanRemoval = true,optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlaceScore placeScores = new PlaceScore();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OpeningDate> openingDates = new ArrayList<>();

    @OneToMany(mappedBy = "place", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Favorite> favorite = new ArrayList<>();

    @Builder
    public Place(String name, String city, String cityDetail, String township, Double latitude, Double longitude, String postCode, String streetAddresses, String telNumber, String url, String placeType, String description, String weightLimit, Boolean parking, Boolean indoor, Boolean outdoor, String thumbImgPath) {
        this.name = name;
        this.city = city;
        this.cityDetail = cityDetail;
        this.township = township;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postCode = postCode;
        this.streetAddresses = streetAddresses;
        this.telNumber = telNumber;
        this.url = url;
        this.placeType = placeType;
        this.description = description;
        this.weightLimit = weightLimit;
        this.parking = parking;
        this.indoor = indoor;
        this.outdoor = outdoor;
        this.thumbImgPath = thumbImgPath;
    }
}
