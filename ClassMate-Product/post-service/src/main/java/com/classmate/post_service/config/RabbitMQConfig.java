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
