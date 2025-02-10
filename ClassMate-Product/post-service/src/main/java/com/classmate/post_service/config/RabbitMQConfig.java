package com.classmate.post_service.config;

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

    @Value("${rabbitmq.queue.delete-forum-queue}")
    private String deleteForumQueue;

    @Value("${rabbitmq.queue.delete-post-file-queue}")
    private String deleteFileQueue;

    @Value("${rabbitmq.queue.delete-post-all-file-queue}")
    private String deletePostAllFileQueue;

    @Value("${rabbitmq.post-exchange.name}")
    private String postExchange;

    @Value("${rabbitmq.file-exchange.name}")
    private String fileExchange;

    @Value("${rabbitmq.exchange.delete-post-routing-key}")
    private String deletePostRoutingKey;

    @Value("${rabbitmq.exchange.delete-forum-routing-key}")
    private String deleteForumRoutingKey;

    @Value("${rabbitmq.delete-post-file.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${rabbitmq.exchange.delete-post-all-file.routing-key}")
    private String deletePostAllFileRoutingKey;

    // NOTIFICATIONS
    @Value("${rabbitmq.queue.notifications.post-author-request-queue}")
    private String postAuthorRequestQueue;
    @Value("${rabbitmq.queue.notifications.post-author-response-queue}")
    private String postAuthorResponseQueue;
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-post-author-request.routing-key}")
    private String postAuthorRequestRoutingKey;
    @Value("${rabbitmq.notifications-post-author-response.routing-key}")
    private String postAuthorResponseRoutingKey;

    // MILESTONE NOTIFICATIONS
    @Value("${rabbitmq.queue.notifications.milestone-queue}")
    private String milestoneNotificationQueue;
    @Value("${rabbitmq.notifications.milestone.routing-key}")
    private String milestoneNotificationRoutingKey;

    // Get forum ID for notifications
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

    @Value("${rabbitmq.queue.comment-count-event-queue}")
    private String commentCountEventQueue;

    @Value("${rabbitmq.queue.create-user-post-service-queue}")
    private String createUserPostServiceQueue;

    @Value("${rabbitmq.queue.create-post-queue}")
    private String createPostQueue;

    @Value("${rabbitmq.queue.delete-user-post-queue}")
    private String deleteUserPostQueue;

    @Value("${rabbitmq.exchange.create-post-exchange.name}")
    private String createPostExchange;

    @Value("${rabbitmq.exchange.create-post.routing-key}")
    private String createPostRoutingKey;

    // User Reputation
    @Value("${rabbitmq.queue.user-reputation-queue}")
    private String userReputationQueue;
    @Value("${rabbitmq.user-reputation-exchange}")
    private String userReputationExchange;
    @Value("${rabbitmq.user-reputation-routing-key}")
    private String userReputationRoutingKey;

    // STATISTICS
    @Value("${rabbitmq.exchange.statistics}")
    private String statisticsExchange;
    @Value("${rabbitmq.queue.post.created.statistic}")
    private String postCreatedStatisticQueue;
    @Value("${rabbitmq.post.created.statistic.routing-key}")
    private String postCreatedStatisticRoutingKey;



    @Bean
    public Queue deletePostQueue() {
        return new Queue(deletePostQueue, true); // durable queue
    }

    @Bean
    public Queue deleteForumQueue() {
        return new Queue(deleteForumQueue, true); // durable queue
    }

    @Bean
    public Queue deleteFileQueue() {
        return new Queue(deleteFileQueue, true);
    }

    @Bean
    public Queue deletePostAllFileQueue() {
        return new Queue(deletePostAllFileQueue, true);
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

    @Bean
    public Queue getCreateUserPostServiceQueue() {
        return new Queue(createUserPostServiceQueue);
    }
    // Comment Count Event Queue
    @Bean
    public Queue getCommentCountEventQueue() {
        return new Queue(commentCountEventQueue, true);
    }

    @Bean
    public TopicExchange postExchange() {
        return new TopicExchange(postExchange);
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


    // NOTIFICATIONS
    @Bean
    public Queue postAuthorRequestQueue() {
        return new Queue(postAuthorRequestQueue, true);
    }

    @Bean
    public Queue postAuthorResponseQueue() {
        return new Queue(postAuthorResponseQueue, true);
    }

    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(notificationsExchange);
    }

    // MILESTONE NOTIFICATIONS
    @Bean
    public Queue milestoneNotificationQueue() {
        return new Queue(milestoneNotificationQueue, true);
    }

    // User Reputation
    @Bean
    public TopicExchange userReputationExchange() {
        return new TopicExchange(userReputationExchange);
    }

    @Bean
    public Queue userReputationQueue() {
        return new Queue(userReputationQueue, true);
    }

    // STATISTICS
    @Bean
    public TopicExchange statisticsExchange() {
        return new TopicExchange(statisticsExchange);
    }

    @Bean
    public Queue postStatisticsQueue() {
        return new Queue(postCreatedStatisticQueue, true);
    }

    @Bean
    public Binding deletePostBinding() {
        return BindingBuilder
                .bind(deletePostQueue())
                .to(postExchange())
                .with(deletePostRoutingKey);
    }



    @Bean
    public Binding deleteForumBinding() {
        return BindingBuilder
                .bind(deleteForumQueue())
                .to(postExchange())
                .with(deleteForumRoutingKey);
    }

    @Bean
    public Binding deleteFileBinding() {
        return BindingBuilder
                .bind(deleteFileQueue())
                .to(fileExchange())
                .with(deleteFileRoutingKey);
    }

    @Bean
    public Binding deletePostAllFileBinding() {
        return BindingBuilder
                .bind(deletePostAllFileQueue())
                .to(fileExchange())
                .with(deletePostAllFileRoutingKey);
    }


    // NOTIFICATIONS
    @Bean
    public Binding postAuthorRequestBinding() {
        return BindingBuilder
                .bind(postAuthorRequestQueue())
                .to(notificationsExchange())
                .with(postAuthorRequestRoutingKey);
    }

    @Bean
    public Binding postAuthorResponseBinding() {
        return BindingBuilder
                .bind(postAuthorResponseQueue())
                .to(notificationsExchange())
                .with(postAuthorResponseRoutingKey);
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

    // User Reputation
    @Bean
    public Binding userReputationBinding() {
        return BindingBuilder
                .bind(userReputationQueue())
                .to(userReputationExchange())
                .with(userReputationRoutingKey);
    }

    // STATISTICS
    @Bean
    public Binding postStatisticsBinding() {
        return BindingBuilder
                .bind(postStatisticsQueue())
                .to(statisticsExchange())
                .with(postCreatedStatisticRoutingKey);
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
