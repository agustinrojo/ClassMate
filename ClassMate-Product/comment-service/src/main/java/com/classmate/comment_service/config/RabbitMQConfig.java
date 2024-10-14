package com.classmate.comment_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.delete-post-queue}")
    private String deletePostQueue;

    @Value("${rabbitmq.queue.delete-file-queue}")
    private String deleteFileQueue;

    @Value("${rabbitmq.queue.delete-comment-queue}")
    private String deleteCommentQueue;

    @Value("${rabbitmq.exchange.name}")
    private String postExchange;

    @Value("${rabbitmq.file-exchange.name}")
    private String fileExchange;

    @Value("${rabbitmq.delete-post-routing-key}")
    private String deletePostRoutingKey;

    @Value("${rabbitmq.delete-file.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${rabbitmq.delete-comment.routing-key}")
    private String deleteCommentRoutingKey;

    // COMMENT NOTIFICATIONS
    @Value("${rabbitmq.queue.notifications.comment-queue}")
    private String commentNotificationQueue;
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-comment.routing-key}")
    private String commentNotificationRoutingKey;

    //Create user queue
    @Value("${rabbitmq.queue.create-user-comment-service-queue}")
    private String createUserCommentServiceQueue;
    @Value("${rabbitmq.create-user-exchange.name}")
    private String createUserExchange;
    @Value("${rabbitmq.create-user-routing-key.name}")
    private String createUserRoutingKey;

    // MILESTONE NOTIFICATIONS
    @Value("${rabbitmq.queue.notifications.milestone-queue}")
    private String milestoneNotificationQueue;
    @Value("${rabbitmq.notifications.milestone.routing-key}")
    private String milestoneNotificationRoutingKey;

    // Get forum ID
    @Value("${rabbitmq.file-exchange.get.forum.id}")
    private String getForumIdExchange;
    @Value("${rabbitmq.queue.notifications.get.forum.id-queue}")
    private String getForumIdNotificationQueue;
    @Value("${rabbitmq.notifications.get.forum.id.routing-key}")
    private String getForumIdNotificationRoutingKey;

    // Get forum ID for notifications RESPONSE
    @Value("${rabbitmq.queue.notifications.get.forum.id.response-queue}")
    private String getForumIdNotificationQueueResponse;
    @Value("${rabbitmq.notifications.get.forum.id.response.routing-key}")
    private String getForumIdNotificationRoutingKeyResponse;

    // Comment Count Event
    //QUEUE
    @Value("${rabbitmq.queue.comment-count-event-queue}")
    private String commentCountEventQueue;
    //EXCHANGE
    @Value("${rabbitmq.exchange.comment-count-event}")
    private String commentCountEventExchange;
    // Routing Key
    @Value("${rabbitmq.comment-count-event.routing-key}")
    private String commentCountEventRoutingKey;

    @Bean
    public Queue deletePostQueue() {
        return new Queue(deletePostQueue, true); // durable queue
    }

    @Bean
    public Queue deleteFileQueue() {
        return new Queue(deleteFileQueue, true);
    }

    @Bean
    public Queue deleteCommentQueue() {
        return new Queue(deleteCommentQueue, true);
    }

    @Bean
    public Queue createUserQueue() {
        return new Queue(createUserCommentServiceQueue, true);
    }

    // MILESTONE NOTIFICATIONS
    @Bean
    public Queue milestoneNotificationQueue() {
        return new Queue(milestoneNotificationQueue, true);
    }

    // GET FORUM ID NOTIFICATIONS
    @Bean
    public Queue getForumIdNotificationQueue() {
        return new Queue(getForumIdNotificationQueue, true);
    }

    // Get forum ID for notifications RESPONSE
    @Bean
    public Queue getForumIdNotificationResponseQueue() {
        return new Queue(getForumIdNotificationQueueResponse, true);
    }

    // Comment Count Queue
    @Bean
    public Queue getCommentCountEventQueue() {
        return new Queue(commentCountEventQueue, true);
    }

    @Bean
    public TopicExchange postExchange() {
        return new TopicExchange(postExchange);
    }

    @Bean
    public TopicExchange createUserExchange(){
        return new TopicExchange(createUserExchange);
    }

    @Bean
    public TopicExchange fileExchange() {
        return new TopicExchange(fileExchange);
    }

    // GET FORUM ID NOTIFICATIONS EXCHANGE
    @Bean
    public TopicExchange getForumIdExchange() {
        return new TopicExchange(getForumIdExchange);
    }

    // Comment Count Exchange
    @Bean
    public TopicExchange getCommentCountExchange(){
        return new TopicExchange(commentCountEventExchange);
    }

    @Bean
    public Binding deletePostBinding() {
        return BindingBuilder
                .bind(deletePostQueue())
                .to(postExchange())
                .with(deletePostRoutingKey);
    }

    @Bean
    public Binding deleteFileBinding() {
        return BindingBuilder
                .bind(deleteFileQueue())
                .to(fileExchange())
                .with(deleteFileRoutingKey);
    }

    @Bean
    public Binding deleteCommentBinding() {
        return BindingBuilder
                .bind(deleteCommentQueue())
                .to(fileExchange())
                .with(deleteCommentRoutingKey);
    }

    // COMMENT NOTIFICATIONS

    @Bean
    public Queue commentNotificationQueue() {
        return new Queue(commentNotificationQueue, true);
    }

    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(notificationsExchange);
    }

    @Bean
    public Binding commentNotificationsBinding() {
        return BindingBuilder
                .bind(commentNotificationQueue())
                .to(notificationsExchange())
                .with(commentNotificationRoutingKey);
    }

    @Bean
    public Binding createUserBinding(){
        return BindingBuilder
                .bind(createUserQueue())
                .to(createUserExchange())
                .with(createUserRoutingKey);
    }

    // MILESTONE NOTIFICATIONS
    @Bean
    public Binding milestoneNotificationBinding() {
        return BindingBuilder
                .bind(milestoneNotificationQueue())
                .to(notificationsExchange())
                .with(milestoneNotificationRoutingKey);
    }

    // GET FORUM ID NOTIFICATIONS
    @Bean
    public Binding getForumIdNotificationBinding() {
        return BindingBuilder
                .bind(getForumIdNotificationQueue())
                .to(getForumIdExchange())
                .with(getForumIdNotificationRoutingKey);
    }

    // Get forum ID for notifications RESPONSE
    @Bean
    public Binding getForumIdNotificationResponseBinding() {
        return BindingBuilder
                .bind(getForumIdNotificationResponseQueue())
                .to(getForumIdExchange())
                .with(getForumIdNotificationRoutingKeyResponse);
    }

    // Comment Count Binding
    @Bean
    public Binding getCommentCountEventBinding() {
        return BindingBuilder
                .bind(getCommentCountEventQueue())
                .to(getCommentCountExchange())
                .with(commentCountEventRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

