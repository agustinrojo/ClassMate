package com.example.statistics_service.config;

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
    @Value("${rabbitmq.exchange.statistics}")
    private String statisticsExchange;

    // POSTS
    @Value("${rabbitmq.queue.post.created.statistic}")
    private String postCreatedStatisticQueue;

    @Value("${rabbitmq.post.created.statistic.routing-key}")
    private String postCreatedStatisticRoutingKey;

    // COMMENTS
    @Value("${rabbitmq.queue.comment.created.statistic}")
    private String commentCreatedStatisticQueue;
    @Value("${rabbitmq.comment.created.statistic.routing-key}")
    private String commentCreatedStatisticRoutingKey;

    // FORUMS CREATION
    @Value("${rabbitmq.queue.forum.created.statistic}")
    private String forumCreatedStatisticQueue;
    @Value("${rabbitmq.forum.created.statistic.routing-key}")
    private String forumCreatedStatisticRoutingKey;


    @Bean
    public TopicExchange statisticsExchange() {
        return new TopicExchange(statisticsExchange);
    }

    @Bean
    public Queue postStatisticsQueue() {
        return new Queue(postCreatedStatisticQueue, true);
    }

    @Bean
    public Queue commentStatisticsQueue() {
        return new Queue(commentCreatedStatisticQueue, true);
    }

    @Bean
    public Queue forumStatisticsQueue() {
        return new Queue(forumCreatedStatisticQueue, true);
    }


    // POSTS
    @Bean
    public Binding postStatisticsBinding() {
        return BindingBuilder
                .bind(postStatisticsQueue())
                .to(statisticsExchange())
                .with(postCreatedStatisticRoutingKey);
    }

    // COMMENTS
    @Bean
    public Binding commentStatisticsBinding() {
        return BindingBuilder
                .bind(commentStatisticsQueue())
                .to(statisticsExchange())
                .with(commentCreatedStatisticRoutingKey);
    }

    // FORUMS CREATION
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

