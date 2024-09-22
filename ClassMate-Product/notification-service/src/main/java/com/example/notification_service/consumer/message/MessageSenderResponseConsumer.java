package com.example.notification_service.consumer.message;

import com.example.notification_service.consumer.comment.PostAuthorResponseConsumer;
import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.message.MessageSenderNameResponseDTO;
import com.example.notification_service.entity.notification.MessageNotification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderResponseConsumer {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(PostAuthorResponseConsumer.class);

    public MessageSenderResponseConsumer(NotificationRepository notificationRepository, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.message-sender-response-queue}")
    public void handleMessageSenderResponse(MessageSenderNameResponseDTO event) {
        // Check if user has this notification type enabled
        if (notificationService.canSendNotification(event.getReceiverId(), "MESSAGE")) {
            // Create and save notification
            MessageNotification notification = new MessageNotification();
            notification.setUserId(event.getReceiverId());
            notification.setIsSeen(false);
            notification.setSenderId(event.getSenderId());
            notification.setSenderName(event.getProfileName());
            notificationRepository.save(notification);

            // Convert to DTO and send via WebSocket
            NotificationDTO notificationDTO = notificationService.convertToDTO(notification);

            LOGGER.info("Sending message notification to user with id: {}", notification.getUserId());

            messagingTemplate.convertAndSend("/topic/notifications/" + event.getReceiverId(), notificationDTO);
        }

    }
}
