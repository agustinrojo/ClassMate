package com.example.notification_service.service;


import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.dto.event.valoration.MilestoneReachedEventDTO;
import com.example.notification_service.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;




public interface NotificationService {

    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);

    void updateNotification(NotificationUpdateDTO notificationUpdateDTO);

    Boolean canSendNotification(Long userId, String notificationType);

    NotificationDTO convertToDTO(Notification notification);

}
