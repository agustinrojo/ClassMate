package com.classmate.post_service.publisher;

import com.classmate.post_service.dto.PostDeletionDTO;
import com.classmate.post_service.dto.filedtos.FileDeletionDTO;
import com.classmate.post_service.dto.filedtos.PostFileDeletionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(PostPublisher.class);

    @Value("${rabbitmq.post-exchange.name}")
    private String exchange;

    @Value("${rabbitmq.file-exchange.name}")
    private String fileExchange;

    @Value("${rabbitmq.exchange.delete-post-routing-key}")
    private String deletePostRoutingKey;

    @Value("${rabbitmq.delete-file.routing-key}")
    private String deletePostFileRoutingKey;

    public PostPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPostDeletion(PostDeletionDTO postDeletionDTO) {
        LOGGER.info(String.format("Post deletion event sent to RabbitMQ -> %s", postDeletionDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, deletePostRoutingKey, postDeletionDTO);
    }

    public void publishPostFileDeleteEvent(PostFileDeletionDTO postFileDeletionDTO) {
        LOGGER.info(String.format("Post file deletion event sent to RabbitMQ -> %s", postFileDeletionDTO.toString()));
        rabbitTemplate.convertAndSend(fileExchange, deletePostFileRoutingKey, postFileDeletionDTO);
    }

    public void publishFileDeleteEvent(FileDeletionDTO event) {
        LOGGER.info(String.format("Post file deletion event sent to RabbitMQ -> %s", event.toString()));
        rabbitTemplate.convertAndSend(fileExchange, deletePostFileRoutingKey, event);
    }
}

