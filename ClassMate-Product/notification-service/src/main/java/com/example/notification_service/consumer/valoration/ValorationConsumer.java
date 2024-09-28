package com.example.notification_service.consumer.valoration;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.valoration.MilestoneReachedEventDTO;
import com.example.notification_service.entity.notification.MilestoneNotification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ValorationConsumer {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ValorationConsumer.class);

    public ValorationConsumer(NotificationService notificationService, NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.milestone-queue}")
    public void handleMilestoneNotification(MilestoneReachedEventDTO event) {
        LOGGER.info("Received milestone event for post ID: {}", event.getPostId());

        // Create the milestone notification
        MilestoneNotification notification = new MilestoneNotification();
        notification.setUserId(event.getAuthorId());
        notification.setMilestone(event.getMilestone());
        notification.setPostId(event.getPostId());
        notification.setForumId(event.getForumId());
        notification.setTitle(event.getTitle());
        notification.setMilestoneType(event.getMilestoneType());
        notification.setIsSeen(false);

        // Save to the database
        notificationRepository.save(notification);

        // Convert to DTO and send via WebSocket
        NotificationDTO notificationDTO = notificationService.convertToDTO(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + event.getAuthorId(), notificationDTO);

        LOGGER.info("Milestone notification sent to user ID: {}", event.getAuthorId());
    }


}
