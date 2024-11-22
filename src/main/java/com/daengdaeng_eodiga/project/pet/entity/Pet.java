package com.daengdaeng_eodiga.project.pet.entity;
import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "Pet")
public class Pet extends BaseEntity {
    @Id
    @Column(name = "pet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PetId;

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

