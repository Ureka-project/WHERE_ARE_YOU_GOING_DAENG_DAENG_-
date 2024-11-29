package com.daengdaeng_eodiga.project.user.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.pet.entity.Pet;

import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.visit.entity.Visit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Setter
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "city_detail")
    private String cityDetail;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "oauth_provider")
    private OauthProvider oauthProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Preference> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

}
