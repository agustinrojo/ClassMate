package com.example.Security.publisher;

import com.example.Security.dto.message.MessageSenderNameResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(NotificationPublisher.class);

    // NOTIFICATIONS
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;

    @Value("${rabbitmq.notifications.message-sender-response.routing-key}")
    private String messageSenderResponseRoutingKey;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    // NOTIFICATIONS
    public void publishMessageSenderNameResponseEvent(MessageSenderNameResponseDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, messageSenderResponseRoutingKey, event);
    }
}
