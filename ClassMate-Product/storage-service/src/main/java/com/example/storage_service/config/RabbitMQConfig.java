package com.example.storage_service.config;

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

    @Value("${rabbitmq.queue.delete-file-queue}")
    private String deleteFileQueue;

    @Value("${rabbitmq.queue.delete-comment-queue}")
    private String deleteCommentQueue;

    @Value("${rabbitmq.queue.delete-post-file-queue}")
    private String deletePostFileQueue;

    @Value("${rabbitmq.queue.delete-post-all-file-queue}")
    private String deletePostAllFileQueue;

    @Value("${rabbitmq.file-exchange.name}")
    private String fileExchange;

    @Value("${rabbitmq.delete-file.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${rabbitmq.delete-comment.routing-key}")
    private String deleteCommentRoutingKey;

    @Value("${rabbitmq.delete-post-file.routing-key}")
    private String deletePostFileRoutingKey;

    @Value("${rabbitmq.exchange.delete-post-all-file.routing-key}")
    private String deletePostAllFileRoutingKey;

    @Bean
    public Queue deleteFileQueue() {
        return new Queue(deleteFileQueue, true);
    }

    @Bean
    public Queue deleteCommentQueue() {
        return new Queue(deleteCommentQueue, true);
    }

    @Bean
    public Queue deletePostFileQueue() {
        return new Queue(deletePostFileQueue, true);
    }

    @Bean
    public Queue deletePostAllFileQueue() {
        return new Queue(deletePostAllFileQueue, true);
    }

    @Bean
    public TopicExchange fileExchange() {
        return new TopicExchange(fileExchange);
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

    @Bean
    public Binding deletePostFileBinding() {
        return BindingBuilder
                .bind(deletePostFileQueue())
                .to(fileExchange())
                .with(deletePostFileRoutingKey);
    }

    @Bean
    public Binding deletePostAllFileBinding() {
        return BindingBuilder
                .bind(deletePostAllFileQueue())
                .to(fileExchange())
                .with(deletePostAllFileRoutingKey);
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
