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
    public TopicExchange postExchange() {
        return new TopicExchange(postExchange);
    }

    @Bean
    public TopicExchange fileExchange() {
        return new TopicExchange(fileExchange);
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

