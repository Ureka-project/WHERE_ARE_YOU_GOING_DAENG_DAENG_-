package com.daengdaeng_eodiga.project.place.dto;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.place.entity.OpeningDate;
import com.daengdaeng_eodiga.project.place.entity.PlaceScore;
import com.daengdaeng_eodiga.project.place.entity.ReviewSummary;
import com.daengdaeng_eodiga.project.place.entity.Visited;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.entity.ReviewKeyword;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class PlaceRcommendDto {

    private Integer placeId;
    private String name;
    private String city;
    private String cityDetail;
    private String township;
    private Double latitude;
    private Double longitude;
    private String postCode;
    private String streetAddresses;
    private String telNumber;
    private String url;
    private String placeType;
    private String description;
    private String weightLimit;
    private Boolean parking;
    private Boolean indoor;
    private Boolean outdoor;
    private Double score;
    private Map<String,Integer> keywords;
    private Long  reviewCount;
    private String imageUrl;
    public PlaceRcommendDto(Integer placeId, String name, String city, String cityDetail, String township,
                            Double latitude, Double longitude, String postCode, String streetAddresses,
                            String telNumber, String url, String placeType, String description, String weightLimit,
                            Boolean parking, Boolean indoor, Boolean outdoor,Double score,  Map<String,Integer> keywords,Long  reviewCount,
                            String imageUrl) {
        this.placeId = placeId;
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
        this.score = score;
        this.keywords =keywords;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
    }

}
