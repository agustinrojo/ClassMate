package com.classmate.forum_service.publisher;

import com.classmate.forum_service.dto.ForumSubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ForumSubscriptionPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(ForumSubscriptionPublisher.class);

    @Value("${rabbitmq.exchange.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.forum-exchange.name}")
    private String exchange;

    public ForumSubscriptionPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSubscription(ForumSubscriptionDTO forumSubscriptionDTO) {
        LOGGER.info(String.format("Forum subscription sent to RabbitMQ -> %s", forumSubscriptionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, forumSubscriptionDTO);
    }
}

