package com.example.chat_v1.service.publisher;

import com.example.chat_v1.dto.publisher.AddChatroomDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(ChatEventsPublisher.class);

    @Value("${rabbitmq.queue.add-chatroom-queue}")
    private String addChatroomQueue;

    @Value("${rabbitmq.chat-exchange.routing-key}")
    private String addChatroomRoutingKey;

    @Value("${rabbitmq.chat-exchange.name}")
    private String chatExchange;

    public ChatEventsPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAddChatroom(AddChatroomDTO addChatroomDTO){
        LOGGER.info(String.format("Sending addChatroomEvent. userId: %d, chatId: %s", addChatroomDTO.getUserId(), addChatroomDTO.getChatId()));
        this.rabbitTemplate.convertAndSend(chatExchange, addChatroomRoutingKey, addChatroomDTO);
    }

}
