package com.example.notification_service.service.impl;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.dto.comment.CommentNotificationResponseDTO;
import com.example.notification_service.dto.event.valoration.MilestoneReachedEventDTO;
import com.example.notification_service.dto.message.MessageNotificationResponseDTO;
import com.example.notification_service.dto.milestone.MilestoneNotificationResponseDTO;
import com.example.notification_service.entity.notification.*;

import com.example.notification_service.repository.NotificationPreferenceRepository;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;


    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationPreferenceRepository preferenceRepository) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
    }

    // Get notifications by userId
    @Override
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreationDateDesc(userId, pageable);

        return notifications.map(this::convertToDTO); // Convert to DTO with map()
    }


    // Before sending a notification, check the user’s preferences
    @Override
    public Boolean canSendNotification(Long userId, String notificationType) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferencesForUser(userId)); // Create or return default preferences

        switch (notificationType) {
            case "COMMENT":
                return preference.getCommentNotificationEnabled();
            case "MILESTONE":
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
        } else if (notification instanceof  MilestoneNotification) {
            return getMilestoneNotificationResponseDTO(notification);
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
                ((CommentNotification) notification).getForumId(),
                ((CommentNotification) notification).getTitle()
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

    private MilestoneNotificationResponseDTO getMilestoneNotificationResponseDTO(Notification notification) {
        return new MilestoneNotificationResponseDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getIsSeen(),
                notification.getCreationDate(),
                ((MilestoneNotification) notification).getMilestone(),
                ((MilestoneNotification) notification).getPostId(),
                ((MilestoneNotification) notification).getForumId(),
                ((MilestoneNotification) notification).getTitle()
        );
    }

    @Override
    public void updateNotification(NotificationUpdateDTO notificationUpdateDTO) {
        Notification notification = notificationRepository.findById(notificationUpdateDTO.getId()).orElseThrow(); //TODO: Exception handling
        notification.setIsSeen(notificationUpdateDTO.getIsSeen());
        notificationRepository.save(notification);
    }


}
