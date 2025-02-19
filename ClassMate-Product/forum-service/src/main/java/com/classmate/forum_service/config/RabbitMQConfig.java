package com.classmate.forum_service.config;

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

    @Value("${rabbitmq.queue.delete-forum-queue}")
    private String deleteForumQueue;

    @Value("${rabbitmq.queue.delete-forum-subscription-queue}")
    private String deleteForumSubscriptionQueue;

    @Value("${rabbitmq.forum-exchange.name}")
    private String exchange;

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

    @Value("${rabbitmq.exchange.delete-forum-routing-key}")
    private String deleteForumRoutingKey;

    @Value("${rabbitmq.exchange.delete-forum-subscription-routing-key}")
    private String deleteForumSubcriptionRoutingKey;

    // Ban Member Events
    @Value("${rabbitmq.queue.ban-user-delete-member-queue}")
    private String banUserDeleteMemberQueue;
    @Value("${rabbitmq.exchange.ban-user-delete-member-routing-key}")
    private String banUserDeleteMemberRoutingKey;

    // FORUMS CREATION STATISTICS
    @Value("${rabbitmq.queue.forum.created.statistic}")
    private String forumCreatedStatisticQueue;
    @Value("${rabbitmq.forum.created.statistic.routing-key}")
    private String forumCreatedStatisticRoutingKey;
    @Value("${rabbitmq.exchange.statistics}")
    private String statisticsExchange;


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
    public Queue deleteForumQueue() {
        return new Queue(deleteForumQueue, true);
    }

    @Bean
    public Queue deleteForumSubcriptionQueue() { return new Queue(deleteForumSubscriptionQueue, true); }

    // Ban members
    @Bean
    public Queue banUserDeleteMemberQueue() {
        return new Queue(banUserDeleteMemberQueue, true);
    }

    // FORUMS CREATION STATISTICS
    @Bean
    public Queue forumStatisticsQueue() {
        return new Queue(forumCreatedStatisticQueue, true);
    }

    @Bean
    public TopicExchange statisticsExchange() {
        return new TopicExchange(statisticsExchange);
    }


    @Bean
    public TopicExchange forumExchange() {
        return new TopicExchange(exchange);
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
    public Binding deleteForumBinding() {
        return BindingBuilder
                .bind(deleteForumQueue())
                .to(forumExchange())
                .with(deleteForumRoutingKey);
    }

    @Bean
    public Binding deleteForumSubscriptionBinding() {
        return BindingBuilder
                .bind(deleteForumSubcriptionQueue())
                .to(forumExchange())
                .with(deleteForumSubcriptionRoutingKey);
    }

    // Ban members
    @Bean
    public Binding banUserDeleteMemberBinding() {
        return BindingBuilder
                .bind(banUserDeleteMemberQueue())
                .to(forumExchange())
                .with(banUserDeleteMemberRoutingKey);
    }

    // FORUMS CREATION STATISTICS
    @Bean
    public Binding forumStatisticsBinding() {
        return BindingBuilder
                .bind(forumStatisticsQueue())
                .to(statisticsExchange())
                .with(forumCreatedStatisticRoutingKey);
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
