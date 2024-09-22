package com.classmate.comment_service.consumer;

import com.classmate.comment_service.dto.user.UserDTO;
import com.classmate.comment_service.entity.User;
import com.classmate.comment_service.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateUserConsumer {
    private final IUserRepository userRepository;

    public CreateUserConsumer(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.create-user-comment-service-queue}")
    public void createUser(UserDTO userDTO){
        User newUser = User.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .build();
        userRepository.save(newUser);
    }
}
