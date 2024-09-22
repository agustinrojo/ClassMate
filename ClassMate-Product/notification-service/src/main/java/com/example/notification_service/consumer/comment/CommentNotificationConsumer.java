package com.example.notification_service.consumer.comment;

import com.example.notification_service.dto.publisherAndConsumerDTOS.comment.CommentNotificationEventDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.comment.PostAuthorRequestEventDTO;
import com.example.notification_service.publisher.NotificationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CommentNotificationConsumer {
    private final NotificationPublisher notificationPublisher;
    private final Logger LOGGER = LoggerFactory.getLogger(CommentNotificationConsumer.class);

    public CommentNotificationConsumer(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.comment-queue}")
    public void handleCommentNotification(CommentNotificationEventDTO event) {
        PostAuthorRequestEventDTO postAuthorRequest = new PostAuthorRequestEventDTO(event.getPostId(), event.getCommentId());
        notificationPublisher.publishPostAuthorRequestEvent(postAuthorRequest);
        LOGGER.info("Requesting post author with post ID: " + postAuthorRequest.getPostId());
    }


}
