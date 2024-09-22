package com.example.notification_service.consumer.comment;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.comment.PostAuthorResponseEventDTO;
import com.example.notification_service.entity.notification.CommentNotification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostAuthorResponseConsumer {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(PostAuthorResponseConsumer.class);

    public PostAuthorResponseConsumer(NotificationRepository notificationRepository, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.post-author-response-queue}")
    public void handlePostAuthorResponse(PostAuthorResponseEventDTO event) {
        // Check if user has this notification type enabled
        if (notificationService.canSendNotification(event.getPostAuthorId(), "COMMENT")) {
            // Create and save notification
            CommentNotification notification = new CommentNotification();
            notification.setUserId(event.getPostAuthorId());
            notification.setIsSeen(false);
            notification.setPostId(event.getPostId());
            notification.setCommentId(event.getCommentId());
            notification.setForumId(event.getForumId());
            notification.setTitle(event.getTitle());
            notificationRepository.save(notification);

            // Convert to DTO and send via WebSocket
            NotificationDTO notificationDTO = notificationService.convertToDTO(notification);

            LOGGER.info("Sending comment notification to user with ID: {}", event.getPostAuthorId());

            messagingTemplate.convertAndSend("/topic/notifications/" + event.getPostAuthorId(), notificationDTO);
        }

    }
}
