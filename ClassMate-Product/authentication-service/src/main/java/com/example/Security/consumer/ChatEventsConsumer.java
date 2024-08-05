package com.example.Security.consumer;

import com.example.Security.dto.chat.AddChatroomDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ChatEventsConsumer {

    private final UserRepository userRepository;


    public ChatEventsConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.add-chatroom-queue}")
    public void addChatroomToUser(AddChatroomDTO addChatroomDTO){
        System.out.println(String.format("Adding chatroom %s to user %d", addChatroomDTO.getChatroomId(), addChatroomDTO.getUserId()));

        User user = userRepository.findById(addChatroomDTO.getUserId())
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", addChatroomDTO.getUserId()));

        user.addChatroom(addChatroomDTO.getChatroomId());
        System.out.println(user.getChatroomIdsIn());
        userRepository.save(user);
    }
}
