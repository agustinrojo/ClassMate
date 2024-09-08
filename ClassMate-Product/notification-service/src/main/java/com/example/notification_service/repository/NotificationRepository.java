package com.example.notification_service.repository;

import com.example.notification_service.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreationDateDesc(Long userId);

}
