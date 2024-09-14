package com.example.chat_v1.config;

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

    @Value("${rabbitmq.queue.add-chatroom-queue}")
    private String addChatroomQueue;

    @Value("${rabbitmq.chat-exchange.routing-key}")
    private String addChatroomRoutingKey;

    @Value("${rabbitmq.chat-exchange.name}")
    private String chatExchange;

    // Chat Messages
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.queue.notifications.message-queue}")
    private String messageNotificationQueue;
    @Value("${rabbitmq.notifications-message.routing-key}")
    private String messageNotificationRoutingKey;


    @Bean
    public Queue addChatroomQueue() {
        return new Queue(addChatroomQueue, true);
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(chatExchange);
    }

    @Bean
    public Binding addChatroomBinding() {
        return BindingBuilder
                .bind(addChatroomQueue())
                .to(chatExchange())
                .with(addChatroomRoutingKey);
    }

    // Chat Messages
    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(notificationsExchange);
    }

    @Bean
    public Queue messageNotificationQueue() {
        return new Queue(messageNotificationQueue, true);
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
