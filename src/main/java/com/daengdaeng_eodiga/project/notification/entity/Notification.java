package com.daengdaeng_eodiga.project.notification.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type;

    private String content;

    @ColumnDefault("false")
    private boolean reading;
}
