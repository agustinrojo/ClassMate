package com.example.Security.consumer;

import com.example.Security.dto.ForumSubscriptionDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class ForumSubscriptionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumSubscriptionConsumer.class);

    private UserRepository userRepository;

    public ForumSubscriptionConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.subscription-queue}")
    public void subscribeToForum(ForumSubscriptionDTO forumSubscriptionDTO){
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(user.isAlreadySubscribedToForum(forumId)){
            LOGGER.error(String.format("User %d already subscribed to forum %d.", userId, forumId));
            return;
        }

        user.subscribeToForum(forumId);
        LOGGER.info(String.format("User %d subscribed to forum %d.", userId, forumId));

        userRepository.save(user);

    }
}
