package com.daengdaeng_eodiga.project.user.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.pet.entity.Pet;

import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog;
import com.daengdaeng_eodiga.project.region.entity.RegionVisitDay;
import com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.visit.entity.Visit;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Preference> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RegionOwnerLog> regionOwnerLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RegionVisitDay> regionVisitDays = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RegionVisitTotal> regionVisitTotals = new ArrayList<>();

    @Builder
    public User (int userId, String nickname, String gender, String email, String city, String cityDetail, String oauthProvider, LocalDateTime deletedAt ) {
        this.userId = userId;
        this.nickname = nickname;
        this.gender = gender;
        this.email = email;
        this.city = city;
        this.cityDetail = cityDetail;
        this.oauthProvider = OauthProvider.valueOf(oauthProvider);
        this.deletedAt = deletedAt;
    }
    public User(){}

}
