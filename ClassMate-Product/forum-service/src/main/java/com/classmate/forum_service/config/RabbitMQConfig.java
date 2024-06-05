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

    @Value("${rabbitmq.forum-exchange.name}")
    private String exchange;

    @Value("${rabbitmq.exchange.routing-key}")
    private String subscriptionRoutingKey;

    @Bean
    public Queue subscriptionQueue() {
        return new Queue(subscriptionQueue, true); // durable queue
    }
    @Bean
    public TopicExchange forumExchange() { return new TopicExchange(exchange); }

    @Bean
    public Binding subscriptionBinding(){
        return BindingBuilder
                .bind(subscriptionQueue())
                .to(forumExchange())
                .with(subscriptionRoutingKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
