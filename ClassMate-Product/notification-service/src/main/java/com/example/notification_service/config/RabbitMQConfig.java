package com.example.notification_service.config;

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

    @Value("${rabbitmq.queue.notifications.comment-queue}")
    private String commentNotificationQueue;
    @Value("${rabbitmq.queue.notifications.post-author-request-queue}")
    private String postAuthorRequestQueue;
    @Value("${rabbitmq.queue.notifications.post-author-response-queue}")
    private String postAuthorResponseQueue;
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-comment.routing-key}")
    private String commentNotificationRoutingKey;
    @Value("${rabbitmq.notifications-post-author-request.routing-key}")
    private String postAuthorRequestRoutingKey;
    @Value("${rabbitmq.notifications-post-author-response.routing-key}")
    private String postAuthorResponseRoutingKey;

    // Chat Messages
    @Value("${rabbitmq.queue.notifications.message-queue}")
    private String messageNotificationQueue;
    @Value("${rabbitmq.notifications-message.routing-key}")
    private String messageNotificationRoutingKey;
    @Value("${rabbitmq.queue.notifications.message-sender-request-queue}")
    private String messageSenderNameRequestQueue;
    @Value("${rabbitmq.queue.notifications.message-sender-response-queue}")
    private String messageSenderNameResponseQueue;
    @Value("${rabbitmq.notifications.message-sender-request.routing-key}")
    private String messageSenderNameRequestRoutingKey;
    @Value("${rabbitmq.notifications.message-sender-response.routing-key}")
    private String messageSenderNameResponseRoutingKey;


    @Bean
    public Queue commentNotificationQueue() {
        return new Queue(commentNotificationQueue, true);
    }

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

    @Bean
    public Binding commentNotificationsBinding() {
        return BindingBuilder
                .bind(commentNotificationQueue())
                .to(notificationsExchange())
                .with(commentNotificationRoutingKey);
    }

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

    // Chat Messages
    @Bean
    public Queue messageNotificationQueue() {
        return new Queue(messageNotificationQueue, true);
    }

    @Bean
    public Queue messageSenderRequestQueue() {
        return new Queue(messageSenderNameRequestQueue, true);
    }

    @Bean
    public Queue messageSenderResponseQueue() {
        return new Queue(messageSenderNameResponseQueue, true);
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
    public Binding messageNotificationBinding() {
        return BindingBuilder
                .bind(messageNotificationQueue())
                .to(notificationsExchange())
                .with(messageNotificationRoutingKey);
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
