package com.classmate.post_service.publisher;

import com.classmate.post_service.dto.CreatePostEvent;
import com.classmate.post_service.dto.PostDeletionDTO;
import com.classmate.post_service.dto.filedtos.FileDeletionDTO;
import com.classmate.post_service.dto.filedtos.PostFileDeletionDTO;
import com.classmate.post_service.dto.notification.GetForumIdNotificationDTOResponse;
import com.classmate.post_service.dto.notification.MilestoneReachedEventDTO;
import com.classmate.post_service.dto.notification.PostAuthorResponseEventDTO;
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

    @Value("${rabbitmq.delete-post-file.routing-key}")
    private String deletePostFileRoutingKey;

    @Value("${rabbitmq.exchange.delete-post-all-file.routing-key}")
    private String deletePostAllFileRoutingKey;

    // NOTIFICATIONS
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;

    @Value("${rabbitmq.notifications-post-author-response.routing-key}")
    private String postAuthorResponseRoutingKey;

    // MILESTONE NOTIFICATIONS
    @Value("${rabbitmq.notifications.milestone.routing-key}")
    private String milestoneNotificationRoutingKey;

    // GET FORUM ID NOTIFICATIONS RESPONSE
    @Value("${rabbitmq.file-exchange.get.forum.id}")
    private String getForumIdExchange;
    @Value("${rabbitmq.notifications.get.forum.id.response.routing-key}")
    private String getForumIdNotificationRoutingKeyResponse;

    @Value("${rabbitmq.exchange.create-post-exchange.name}")
    private String createPostExchange;

    @Value("${rabbitmq.exchange.create-post.routing-key}")
    private String createPostRoutingKey;

    public PostPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPostDeletion(PostDeletionDTO postDeletionDTO) {
        if (postDeletionDTO != null) {
            LOGGER.info(String.format("Post deletion event sent to RabbitMQ -> %s", postDeletionDTO.toString()));
            rabbitTemplate.convertAndSend(exchange, deletePostRoutingKey, postDeletionDTO);
        } else {
            LOGGER.error("PostDeletionDTO is null, skipping event publication");
        }
    }

    public void publishPostAllFileDeleteEvent(PostFileDeletionDTO postFileDeletionDTO) {
        if (postFileDeletionDTO != null) {
            LOGGER.info(String.format("Post all file deletion event sent to RabbitMQ -> %s", postFileDeletionDTO.toString()));
            rabbitTemplate.convertAndSend(fileExchange, deletePostAllFileRoutingKey, postFileDeletionDTO);
        } else {
            LOGGER.error("PostFileDeletionDTO is null, skipping event publication");
        }
    }

    public void publishPostFileDeleteEvent(FileDeletionDTO event) {
        if (event != null) {
            LOGGER.info(String.format("Post file deletion event sent to RabbitMQ -> %s", event.getFileId().toString()));
            rabbitTemplate.convertAndSend(fileExchange, deletePostFileRoutingKey, event);
        } else {
            LOGGER.error("FileDeletionDTO is null, skipping event publication");
        }
    }

    // NOTIFICATIONS
    public void publishPostAuthorResponseEvent(PostAuthorResponseEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, postAuthorResponseRoutingKey, event);
    }

    // VALORATIONS
    public void publishMilestoneReachedEvent(MilestoneReachedEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, milestoneNotificationRoutingKey, event);
    }

    // GET FORUM ID VALORATIONS
    public void publishGetForumIdResponseEvent(GetForumIdNotificationDTOResponse event) {
        rabbitTemplate.convertAndSend(getForumIdExchange, getForumIdNotificationRoutingKeyResponse, event);
    }


}
