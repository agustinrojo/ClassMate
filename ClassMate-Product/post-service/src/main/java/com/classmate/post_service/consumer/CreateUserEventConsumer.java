package com.classmate.post_service.consumer;

import com.classmate.post_service.dto.user.UserDTO;
import com.classmate.post_service.entity.User;
import com.classmate.post_service.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateUserEventConsumer {
    private IUserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(CreateUserEventConsumer.class);

    public CreateUserEventConsumer(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.create-user-post-service-queue}")
    public void consumeCreateUserEvent(UserDTO userDTO){
        LOGGER.info(String.format("Saving user with id: %d and nickname: %s", userDTO.getUserId(), userDTO.getNickname()));
        User user = User.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .build();

        userRepository.save(user);
    }
}
