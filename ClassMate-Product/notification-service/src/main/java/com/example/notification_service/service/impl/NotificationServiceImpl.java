package com.example.notification_service.service.impl;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.dto.comment.CommentNotificationResponseDTO;
import com.example.notification_service.dto.message.MessageNotificationResponseDTO;
import com.example.notification_service.entity.notification.CommentNotification;

import com.example.notification_service.entity.notification.MessageNotification;
import com.example.notification_service.entity.notification.Notification;
import com.example.notification_service.entity.notification.NotificationPreference;
import com.example.notification_service.repository.NotificationPreferenceRepository;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationServiceImpl implements NotificationService {
    Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;


    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationPreferenceRepository preferenceRepository) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
    }

    // Get notifications by userId
    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        List<Notification> notifications= notificationRepository.findByUserIdOrderByCreationDateDesc(userId);

        // Convert notifications to DTOs based on their type using polymorphism
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Before sending a notification, check the user’s preferences
    @Override
    public Boolean canSendNotification(Long userId, String notificationType) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferencesForUser(userId)); // Create or return default preferences

        switch (notificationType) {
            case "COMMENT":
                return preference.getCommentNotificationEnabled();
            case "LIKE":
                return preference.getLikeNotificationEnabled();
            case "MESSAGE":
                return preference.getMessageNotificationEnabled();
            case "EVENT":
                return preference.getEventNotificationEnabled();
            default:
                return false;
        }
    }

    private NotificationPreference createDefaultPreferencesForUser(Long userId) {
        NotificationPreference defaultPreferences = new NotificationPreference();
        defaultPreferences.setUserId(userId);
        defaultPreferences.setCommentNotificationEnabled(true);
        defaultPreferences.setEventNotificationEnabled(true);
        defaultPreferences.setLikeNotificationEnabled(true);
        defaultPreferences.setMessageNotificationEnabled(true);
        return preferenceRepository.save(defaultPreferences);
    }

    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        if(notification instanceof CommentNotification) {
            return getCommentNotificationResponseDTO(notification);
        } else if (notification instanceof MessageNotification) {
            return getMessageNotificationResponseDTO(notification);
        }

        throw new IllegalArgumentException("Unknown notification type: " + notification.getClass().getSimpleName());
    }

    private CommentNotificationResponseDTO getCommentNotificationResponseDTO(Notification notification) {
        return new CommentNotificationResponseDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getIsSeen(),
                notification.getCreationDate(),
                ((CommentNotification) notification).getPostId(),
                ((CommentNotification) notification).getCommentId(),
                ((CommentNotification) notification).getForumId()
        );
    }

    private MessageNotificationResponseDTO getMessageNotificationResponseDTO(Notification notification) {
        return new MessageNotificationResponseDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getIsSeen(),
                notification.getCreationDate(),
                ((MessageNotification) notification).getSenderId(),
                ((MessageNotification) notification).getSenderName()
        );
    }

    @Override
    public void updateNotification(NotificationUpdateDTO notificationUpdateDTO) {
        Notification notification = notificationRepository.findById(notificationUpdateDTO.getId()).orElseThrow(); //TODO: Exception handling
        notification.setIsSeen(notificationUpdateDTO.getIsSeen());
        notificationRepository.save(notification);
    }

}
