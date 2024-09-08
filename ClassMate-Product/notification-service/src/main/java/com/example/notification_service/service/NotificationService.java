package com.example.notification_service.service;


import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.entity.notification.Notification;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getUserNotifications(Long userId);

    void updateNotification(NotificationUpdateDTO notificationUpdateDTO);

    Boolean canSendNotification(Long userId, String notificationType);

    NotificationDTO convertToDTO(Notification notification);
}
