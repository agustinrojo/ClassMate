package com.classmate.comment_service.publisher;

import com.classmate.comment_service.dto.CommentDeletionDTO;
import com.classmate.comment_service.dto.filedtos.FileDeletionDTO;
import com.classmate.comment_service.dto.notifications.CommentNotificationEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.file-exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delete-file.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${rabbitmq.delete-comment.routing-key}")
    private String deleteCommentRoutingKey;

    // COMMENT NOTIFICATIONS
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-comment.routing-key}")
    private String commentNotificationRoutingKey;

    public CommentPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishFileDeleteEvent(FileDeletionDTO event) {
        rabbitTemplate.convertAndSend(exchange, deleteFileRoutingKey, event);
    }

    public void publishCommentDeleteEvent(CommentDeletionDTO event) {
        rabbitTemplate.convertAndSend(exchange, deleteCommentRoutingKey, event);
    }

    // COMMENT NOTIFICATIONS
    public void publishCommentNotificationEvent(CommentNotificationEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, commentNotificationRoutingKey, event);
    }
}
