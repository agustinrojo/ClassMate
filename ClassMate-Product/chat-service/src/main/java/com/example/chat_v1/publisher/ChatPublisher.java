package com.example.chat_v1.publisher;

import com.example.chat_v1.dto.message.MessageNotificationEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatPublisher.class);

    // MESSAGE NOTIFICATIONS
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-message.routing-key}")
    private String messageNotificationRoutingKey;

    public ChatPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // MESSAGE NOTIFICATIONS
    public void publishMessageNotificationEvent(MessageNotificationEventDTO event) {
        LOGGER.info("Notification message event sent with receiver id: {} and sender id: {}", event.getReceiverId(), event.getSenderId());
        rabbitTemplate.convertAndSend(notificationsExchange, messageNotificationRoutingKey, event);
    }
}
