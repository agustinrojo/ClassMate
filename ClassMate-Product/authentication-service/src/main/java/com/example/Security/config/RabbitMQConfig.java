package com.example.Security.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.subscription-queue}")
    private String subscriptionQueue;

    @Value("${rabbitmq.queue.add-admin-queue}")
    private String addAdminQueue;

    @Value("${rabbitmq.queue.remove-member-queue}")
    private String removeMemberQueue;

    @Value("${rabbitmq.queue.remove-admin-queue}")
    private String removeAdminQueue;

    @Value("${rabbitmq.queue.creator-update-queue}")
    private String creatorUpdateQueue;

    @Value("${rabbitmq.queue.delete-forum-subscription-queue}")
    private String deleteForumSubscriptionQueue;

    @Value("${rabbitmq.queue.add-chatroom-queue}")
    private String addChatroomQueue;

    @Value("${rabbitmq.queue.create-user-comment-service-queue}")
    private String createUserCommentServiceQueue;

    @Value("${rabbitmq.queue.create-user-post-service-queue}")
    private String createUserPostServiceQueue;

    @Value("${rabbitmq.queue.create-user-forum-service-queue}")
    private String createUserForumServiceQueue;

    @Value("${rabbitmq.chat-exchange.routing-key}")
    private String addChatroomRoutingKey;

    @Value("${rabbitmq.chat-exchange.name}")
    private String chatExchange;

    @Value("${rabbitmq.forum-exchange.name}")
    private String exchange;

    @Value("${rabbitmq.create-user-exchange.name}")
    private String createUserExchange;

    @Value("${rabbitmq.exchange.routing-key}")
    private String subscriptionRoutingKey;

    @Value("${rabbitmq.exchange.add-admin-routing-key}")
    private String addAdminRoutingKey;

    @Value("${rabbitmq.exchange.remove-member-routing-key}")
    private String removeMemberRoutingKey;

    @Value("${rabbitmq.exchange.remove-admin-routing-key}")
    private String removeAdminRoutingKey;

    @Value("${rabbitmq.exchange.creator-update-routing-key}")
    private String creatorUpdateRoutingKey;

    @Value("${rabbitmq.exchange.delete-forum-subscription-routing-key}")
    private String deleteForumSubscriptionRoutingKey;

    // Chat Messages
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.queue.notifications.message-sender-request-queue}")
    private String messageSenderNameRequestQueue;
    @Value("${rabbitmq.queue.notifications.message-sender-response-queue}")
    private String messageSenderNameResponseQueue;
    @Value("${rabbitmq.notifications.message-sender-request.routing-key}")
    private String messageSenderNameRequestRoutingKey;
    @Value("${rabbitmq.notifications.message-sender-response.routing-key}")
    private String messageSenderNameResponseRoutingKey;
    @Value("${rabbitmq.create-user-routing-key.name}")
    private String createUserRoutingKey;

    // Ban Member Events
    @Value("${rabbitmq.queue.ban-user-delete-member-queue}")
    private String banUserDeleteMemberQueue;
    @Value("${rabbitmq.exchange.ban-user-delete-member-routing-key}")
    private String banUserDeleteMemberRoutingKey;

    // User Reputation
    @Value("${rabbitmq.queue.user-reputation-queue}")
    private String userReputationQueue;
    @Value("${rabbitmq.user-reputation-exchange}")
    private String userReputationExchange;
    @Value("${rabbitmq.user-reputation-routing-key}")
    private String userReputationRoutingKey;

    // USER STATISTICS
    @Value("${rabbitmq.exchange.statistics}")
    private String statisticsExchange;
    @Value("${rabbitmq.queue.user.created.statistic}")
    private String userCreatedStatisticQueue;
    @Value("${rabbitmq.user.created.statistic.routing-key}")
    private String userCreatedStatisticRoutingKey;
    @Value("${rabbitmq.queue.user.logged.statistic}")
    private String userLoggedStatisticQueue;
    @Value("${rabbitmq.user.logged.statistic.routing-key}")
    private String userLoggedStatisticRoutingKey;

    @Bean
    public Queue subscriptionQueue() {
        return new Queue(subscriptionQueue, true);
    }

    @Bean
    public Queue addAdminQueue() {
        return new Queue(addAdminQueue, true);
    }

    @Bean
    public Queue removeMemberQueue() {
        return new Queue(removeMemberQueue, true);
    }

    @Bean
    public Queue removeAdminQueue() {
        return new Queue(removeAdminQueue, true);
    }

    @Bean
    public Queue creatorUpdateQueue() {
        return new Queue(creatorUpdateQueue, true);
    }

    @Bean
    public Queue deleteForumSubscriptionQueue(){
        return new Queue(deleteForumSubscriptionQueue, true);
    }

    @Bean
    public Queue addChatroomQueue() {
        return new Queue(addChatroomQueue, true);
    }

    @Bean
    public Queue createUserCommentServiceQueue() { return new Queue(createUserCommentServiceQueue, true); }

    @Bean
    public Queue createUserPostServiceQueue() { return new Queue(createUserPostServiceQueue, true); }

    @Bean
    public Queue createUserForumServiceQueue() { return new Queue(createUserForumServiceQueue, true); }

    @Bean
    public Queue messageSenderRequestQueue() {
        return new Queue(messageSenderNameRequestQueue, true);
    }

    @Bean
    public Queue messageSenderResponseQueue() {
        return new Queue(messageSenderNameResponseQueue, true);
    }

    // Ban members
    @Bean
    public Queue banUserDeleteMemberQueue() {
        return new Queue(banUserDeleteMemberQueue, true);
    }

    @Bean
    public TopicExchange forumExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(chatExchange);
    }

    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(notificationsExchange);
    }

    @Bean
    public TopicExchange createUserExchange() {
        return new TopicExchange(createUserExchange);
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

    // USERS STATISTICS
    @Bean
    public TopicExchange statisticsExchange() {
        return new TopicExchange(statisticsExchange);
    }

    @Bean
    public Queue userCreatedStatisticsQueue() {
        return new Queue(userCreatedStatisticQueue, true);
    }

    @Bean
    public Queue userLoggedStatisticsQueue() {
        return new Queue(userLoggedStatisticQueue, true);
    }


    @Bean
    public Binding subscriptionBinding() {
        return BindingBuilder
                .bind(subscriptionQueue())
                .to(forumExchange())
                .with(subscriptionRoutingKey);
    }

    @Bean
    public Binding addAdminBinding() {
        return BindingBuilder
                .bind(addAdminQueue())
                .to(forumExchange())
                .with(addAdminRoutingKey);
    }

    @Bean
    public Binding removeMemberBinding() {
        return BindingBuilder
                .bind(removeMemberQueue())
                .to(forumExchange())
                .with(removeMemberRoutingKey);
    }

    @Bean
    public Binding removeAdminBinding() {
        return BindingBuilder
                .bind(removeAdminQueue())
                .to(forumExchange())
                .with(removeAdminRoutingKey);
    }

    @Bean
    public Binding creatorUpdateBinding() {
        return BindingBuilder
                .bind(creatorUpdateQueue())
                .to(forumExchange())
                .with(creatorUpdateRoutingKey);
    }

    @Bean
    public Binding deleteForumBinding(){
        return BindingBuilder
                .bind(deleteForumSubscriptionQueue())
                .to(forumExchange())
                .with(deleteForumSubscriptionRoutingKey);
    }
    @Bean
    public Binding addChatroomBinding() {
        return BindingBuilder
                .bind(addChatroomQueue())
                .to(chatExchange())
                .with(addChatroomRoutingKey);
    }

    @Bean
    public Binding messageSenderRequestBinding() {
        return BindingBuilder
                .bind(messageSenderRequestQueue())
                .to(notificationsExchange())
                .with(messageSenderNameRequestRoutingKey);
    }

    @Bean
    public Binding messageSenderResponseBinding() {
        return BindingBuilder
                .bind(messageSenderResponseQueue())
                .to(notificationsExchange())
                .with(messageSenderNameResponseRoutingKey);
    }

    @Bean
    public Binding createUserCommentServiceBinding() {
        return BindingBuilder
                .bind(createUserCommentServiceQueue())
                .to(createUserExchange())
                .with(createUserRoutingKey);
    }

    @Bean
    public Binding createUserPostServiceBinding(){
        return BindingBuilder
                .bind(createUserPostServiceQueue())
                .to(createUserExchange())
                .with(createUserRoutingKey);
    }

    // Ban members
    @Bean
    public Binding banUserDeleteMemberBinding() {
        return BindingBuilder
                .bind(banUserDeleteMemberQueue())
                .to(forumExchange())
                .with(banUserDeleteMemberRoutingKey);
    }

    // User Reputation
    @Bean
    public Binding userReputationBinding() {
        return BindingBuilder
                .bind(userReputationQueue())
                .to(userReputationExchange())
                .with(userReputationRoutingKey);
    }

    // USER STATISTICS
    @Bean
    public Binding userCreatedStatisticsBinding() {
        return BindingBuilder
                .bind(userCreatedStatisticsQueue())
                .to(statisticsExchange())
                .with(userCreatedStatisticRoutingKey);
    }

    @Bean
    public Binding userLoggedStatisticsBinding() {
        return BindingBuilder
                .bind(userLoggedStatisticsQueue())
                .to(statisticsExchange())
                .with(userLoggedStatisticRoutingKey);
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
