package com.daengdaeng_eodiga.project.domain.User.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Preference")
public class Preference {

    @EmbeddedId
    private PreferenceId id; // 복합 기본 키

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user; // 외래 키 관계

    @Column(name = "preference_type", length = 255)
    private String preferenceType;

    // Getter, Setter, toString 등 필요 시 추가
}
