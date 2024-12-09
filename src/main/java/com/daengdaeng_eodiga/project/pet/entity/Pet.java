package com.daengdaeng_eodiga.project.pet.entity;
import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.review.entity.ReviewPet;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.visit.entity.VisitPet;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "Pet")
public class Pet extends BaseEntity {
    @Id
    @Column(name = "pet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private String image;

    private String gender;

    private Date birthday;

    private String species;

    private String size;
    @ColumnDefault("false")
    private Boolean neutering;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ReviewPet> reviewPets = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VisitPet> visitPets = new ArrayList<>();

    @Builder
    public Pet(User user, String name, String image, String gender, Date birthday, String species, String size, Boolean neutering) {
        this.user = user;
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.birthday = birthday;
        this.species = species;
        this.size = size;
        this.neutering = neutering;
    }

    public Pet() {}
}

