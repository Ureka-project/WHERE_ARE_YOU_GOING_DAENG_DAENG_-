package com.daengdaeng_eodiga.project.domain.User.entity;

import com.daengdaeng_eodiga.project.domain.Favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.domain.Pet.entity.Pet;
import com.daengdaeng_eodiga.project.domain.Place.entity.Visited;
import com.daengdaeng_eodiga.project.domain.Review.entity.Review;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
@Entity
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "name", nullable = false)  // NOT NULL 제약조건
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false)  // NOT NULL 제약조건
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "city_detail")
    private String cityDetail;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Preference> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visited> visitieds = new ArrayList<>();

    // Getter, Setter
}
