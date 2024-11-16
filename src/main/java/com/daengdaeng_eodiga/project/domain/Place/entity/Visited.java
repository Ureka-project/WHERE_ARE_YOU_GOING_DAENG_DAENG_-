package com.daengdaeng_eodiga.project.domain.Place.entity;

import com.daengdaeng_eodiga.project.domain.Pet.entity.Pet;
import com.daengdaeng_eodiga.project.domain.User.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Visited")
public class Visited {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Getter, Setter
}
