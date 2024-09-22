package com.example.notification_service.service.impl;

import com.example.notification_service.dto.APIDTOS.calendarEvent.EventNotificationResponseDTO;
import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.dto.APIDTOS.comment.CommentNotificationResponseDTO;
import com.example.notification_service.dto.APIDTOS.message.MessageNotificationResponseDTO;
import com.example.notification_service.dto.APIDTOS.milestone.MilestoneNotificationResponseDTO;
import com.example.notification_service.entity.notification.*;

import com.example.notification_service.repository.NotificationPreferenceRepository;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.aspectj.weaver.ast.Not;
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
        return switch (notification.getClass().getSimpleName()) {
            case "CommentNotification" -> getCommentNotificationResponseDTO(notification);
            case "MessageNotification" -> getMessageNotificationResponseDTO(notification);
            case "MilestoneNotification" -> getMilestoneNotificationResponseDTO(notification);
            case "EventNotification" -> getEventNotificationResponseDTO(notification);
            default -> throw new IllegalArgumentException("Unknown notification type: " + notification.getClass().getSimpleName());
        };
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

    private EventNotificationResponseDTO getEventNotificationResponseDTO(Notification notification) {
        return new EventNotificationResponseDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getIsSeen(),
                notification.getCreationDate(),
                ((EventNotification) notification).getEventTitle(),
                ((EventNotification) notification).getStartDate()
        );
    }


    @Override
    public void updateNotification(NotificationUpdateDTO notificationUpdateDTO) {
        Notification notification = notificationRepository.findById(notificationUpdateDTO.getId()).orElseThrow(); //TODO: Exception handling
        notification.setIsSeen(notificationUpdateDTO.getIsSeen());
        notificationRepository.save(notification);
    }


}
