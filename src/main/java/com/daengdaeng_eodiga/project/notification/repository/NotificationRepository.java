package com.daengdaeng_eodiga.project.notification.repository;

import com.daengdaeng_eodiga.project.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUser_UserIdAndReadingFalseOrderByCreatedAtDesc(int userId);
}