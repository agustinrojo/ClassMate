package com.classmate.post_service.publisher;

import com.classmate.post_service.dto.PostDeletionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(PostPublisher.class);

    @Value("${rabbitmq.exchange.delete-post-routing-key}")
    private String deletePostRoutingKey;

    @Value("${rabbitmq.post-exchange.name}")
    private String exchange;

    public PostPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPostDeletion(PostDeletionDTO postDeletionDTO) {
        LOGGER.info(String.format("Post deletion event sent to RabbitMQ -> %s", postDeletionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, deletePostRoutingKey, postDeletionDTO);
    }
}

