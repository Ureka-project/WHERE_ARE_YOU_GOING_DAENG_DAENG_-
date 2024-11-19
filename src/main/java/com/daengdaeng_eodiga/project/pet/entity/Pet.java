package com.daengdaeng_eodiga.project.pet.entity;
import com.daengdaeng_eodiga.project.user.entity.User;
import jakarta.persistence.*;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "Pet")
public class Pet {
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
}

