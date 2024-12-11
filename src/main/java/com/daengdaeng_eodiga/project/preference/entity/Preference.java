    package com.daengdaeng_eodiga.project.preference.entity;

    import com.daengdaeng_eodiga.project.user.entity.User;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import lombok.Setter;

    @Entity
    @Getter
    @Setter
    @RequiredArgsConstructor
    @Table(name = "Preference")
    public class Preference {

        @EmbeddedId
        private PreferenceId id;

        @MapsId("userId")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", insertable = false, updatable = false)
        private User user;

        @Column(name = "preference_type")
        private String preferenceType;

    }
