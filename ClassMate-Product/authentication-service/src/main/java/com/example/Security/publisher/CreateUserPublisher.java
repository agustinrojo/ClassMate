package com.example.Security.publisher;

import com.example.Security.dto.user.UserDisplayDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateUserPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(CreateUserPublisher.class);
    @Value("${rabbitmq.create-user-routing-key.name}")
    private String createUserRoutingKey;
    @Value("${rabbitmq.create-user-exchange.name}")
    private String createUserExchange;

    public CreateUserPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreateUserEvent(UserDisplayDTO userDisplayDTO){
        rabbitTemplate.convertAndSend(createUserExchange, createUserRoutingKey, userDisplayDTO);
    }


}
