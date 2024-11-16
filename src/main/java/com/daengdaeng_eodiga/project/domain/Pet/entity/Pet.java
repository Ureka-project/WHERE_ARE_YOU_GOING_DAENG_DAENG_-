package com.daengdaeng_eodiga.project.domain.Pet.entity;
import com.daengdaeng_eodiga.project.domain.User.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "species")
    private String species;

    @Column(name = "size")
    private String size;

    @Column(name = "neutering")
    private Boolean neutering;  // BOOLEAN 타입, null 허용
    // Getter, Setter
}

