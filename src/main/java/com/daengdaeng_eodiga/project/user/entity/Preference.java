package com.daengdaeng_eodiga.project.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Preference")
public class Preference {

    @EmbeddedId
    private PreferenceId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "preference_type")
    private String preferenceType;

}
