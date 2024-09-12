package com.example.calendar_service.config;

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
    @Value("${rabbitmq.queue.sync-google-queue}")
    private String syncGoogleQueue;

    @Value("${rabbitmq.queue.unsync-google-queue}")
    private String unsyncGoogleQueue;

    @Value("${rabbitmq.sync-exchange.name}")
    private String syncExchange;

    @Value("${rabbitmq.exchange.sync-google-routing-key}")
    private String syncRoutingKey;

    @Bean
    public Queue syncGoogleQueue() {
        return new Queue(syncGoogleQueue, true);
    }

    @Bean
    public Queue unsyncGoogleQueue(){
        return new Queue(unsyncGoogleQueue, true);
    }

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(syncExchange);
    }

    @Bean
    public Binding syncBinding(){
        return BindingBuilder
                .bind(syncGoogleQueue())
                .to(syncExchange())
                .with(syncRoutingKey);
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
