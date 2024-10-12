package com.classmate.post_service.consumer;

import com.classmate.post_service.dto.user.UserDTO;
import com.classmate.post_service.entity.User;
import com.classmate.post_service.mapper.IUserMapper;
import com.classmate.post_service.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateUserConsumer {
    private IUserRepository userRepository;
    private IUserMapper userMapper;
    private Logger LOGGER = LoggerFactory.getLogger(CreateUserConsumer.class);

    public CreateUserConsumer(IUserRepository userRepository, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.create-user-post-service-queue}")
    @Transactional
    public void createUser(UserDTO userDTO){
        LOGGER.info("Saving user: " + userDTO);
        User user = userMapper.mapUserDTOToUser(userDTO);
        userRepository.save(user);
    }
}
