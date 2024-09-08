package com.example.notification_service.repository;

import com.example.notification_service.entity.notification.MessageNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageNotificationRepository extends JpaRepository<MessageNotification, Long> {

    // Check if there's an unread message notification from the same sender
    boolean existsByUserIdAndSenderIdAndIsSeenFalse(Long userId, Long senderId);
}
