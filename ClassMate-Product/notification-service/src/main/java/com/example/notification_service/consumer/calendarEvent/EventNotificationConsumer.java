package com.example.notification_service.consumer.calendarEvent;

import com.example.notification_service.dto.APIDTOS.calendarEvent.EventNotificationResponseDTO;
import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.calendarEvent.EventNotificationDTO;
import com.example.notification_service.entity.notification.EventNotification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventNotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public EventNotificationConsumer(NotificationRepository notificationRepository, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.event-queue}")
    public void handleEventNotification(EventNotificationDTO event) {

        if (notificationService.canSendNotification(event.getUserId(), "EVENT")) {
            EventNotification notification = new EventNotification();
            notification.setUserId(event.getUserId());
            notification.setEventTitle(event.getTitle());
            notification.setStartDate(event.getStartDate());
            notification.setIsSeen(false);
            notificationRepository.save(notification);

            // Convert to DTO and send via WebSocket
            NotificationDTO notificationDTO = notificationService.convertToDTO(notification);
            messagingTemplate.convertAndSend("/topic/notifications/" + event.getUserId(), notificationDTO);
        }

    }
}
