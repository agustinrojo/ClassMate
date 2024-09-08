package com.example.notification_service.publisher;

import com.example.notification_service.dto.event.comment.PostAuthorRequestEventDTO;
import com.example.notification_service.dto.event.message.MessageSenderNameRequestDTO;
import com.example.notification_service.dto.event.valoration.MilestoneReachedEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;

    @Value("${rabbitmq.notifications-post-author-request.routing-key}")
    private String postAuthorRequestRoutingKey;

    // Chat Messages
    @Value("${rabbitmq.notifications.message-sender-request.routing-key}")
    private String messageSenderNameRequestRoutingKey;


    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPostAuthorRequestEvent(PostAuthorRequestEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, postAuthorRequestRoutingKey, event);
    }

    public void publishMessageSenderNameRequestEvent(MessageSenderNameRequestDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, messageSenderNameRequestRoutingKey, event);
    }

}
