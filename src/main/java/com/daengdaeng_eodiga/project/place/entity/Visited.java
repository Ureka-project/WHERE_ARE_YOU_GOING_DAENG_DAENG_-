package com.daengdaeng_eodiga.project.place.entity;

import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.user.entity.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Visited")
public class Visited {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visited_id")
    private Integer visitedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = true)
    private Pet pet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "visited_at")
    private Date visitedAt;

}
