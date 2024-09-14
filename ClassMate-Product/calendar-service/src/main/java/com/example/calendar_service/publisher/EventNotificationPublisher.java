package com.example.calendar_service.publisher;

import com.example.calendar_service.dto.EventNotificationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventNotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.notifications.event.routing-key}")
    private String eventNotificationRoutingKey;

    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;


    public EventNotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEventNotification(EventNotificationDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, eventNotificationRoutingKey, event);
    }
}
