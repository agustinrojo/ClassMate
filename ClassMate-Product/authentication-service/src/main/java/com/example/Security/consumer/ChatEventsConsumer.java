package com.example.Security.consumer;

import com.example.Security.dto.chat.AddChatroomDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ChatEventsConsumer {
    private UserRepository userRepository;

    public ChatEventsConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.add-chatroom-queue}")
    private void addChatRoom(AddChatroomDTO addChatroomDTO){
        Long userId = addChatroomDTO.getUserId();
        String chatId = addChatroomDTO.getChatId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("user", "id", userId));

        user.addChatroom(chatId);
        userRepository.save(user);
    }
}
