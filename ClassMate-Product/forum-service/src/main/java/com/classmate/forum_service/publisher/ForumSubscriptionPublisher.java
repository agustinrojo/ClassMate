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
    private String subscriptionRoutingKey;

    @Value("${rabbitmq.exchange.add-admin-routing-key}")
    private String addAdminRoutingKey;

    @Value("${rabbitmq.exchange.remove-member-routing-key}")
    private String removeMemberRoutingKey;

    @Value("${rabbitmq.exchange.remove-admin-routing-key}")
    private String removeAdminRoutingKey;

    @Value("${rabbitmq.forum-exchange.name}")
    private String exchange;

    public ForumSubscriptionPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSubscription(ForumSubscriptionDTO forumSubscriptionDTO) {
        LOGGER.info(String.format("Forum subscription sent to RabbitMQ -> %s", forumSubscriptionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, subscriptionRoutingKey, forumSubscriptionDTO);
    }

    public void publishAddAdmin(ForumSubscriptionDTO forumSubscriptionDTO) {
        LOGGER.info(String.format("Add admin event sent to RabbitMQ -> %s", forumSubscriptionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, addAdminRoutingKey, forumSubscriptionDTO);
    }

    public void publishRemoveMember(ForumSubscriptionDTO forumSubscriptionDTO) {
        LOGGER.info(String.format("Remove member event sent to RabbitMQ -> %s", forumSubscriptionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, removeMemberRoutingKey, forumSubscriptionDTO);
    }

    public void publishRemoveAdmin(ForumSubscriptionDTO forumSubscriptionDTO) {
        LOGGER.info(String.format("Remove admin event sent to RabbitMQ -> %s", forumSubscriptionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, removeAdminRoutingKey, forumSubscriptionDTO);
    }
}
