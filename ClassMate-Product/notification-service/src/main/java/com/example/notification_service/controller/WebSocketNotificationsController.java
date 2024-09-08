package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationsController {
    private final NotificationService notificationService;

    public WebSocketNotificationsController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Send notification to front end
    @MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public NotificationDTO sendNotification(NotificationDTO notificationDTO) {
        return notificationDTO;
    }
}
