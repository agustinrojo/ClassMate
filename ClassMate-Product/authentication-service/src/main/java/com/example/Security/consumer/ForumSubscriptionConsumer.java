package com.example.Security.consumer;

import com.example.Security.dto.ForumSubscriptionDTO;
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
@Slf4j
public class ForumSubscriptionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumSubscriptionConsumer.class);

    private final UserRepository userRepository;

    public ForumSubscriptionConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
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

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.add-admin-queue}")
    public void addAdminToForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(user.isAlreadyAdminOfForum(forumId)){
            LOGGER.error(String.format("User %d is already an admin of forum %d.", userId, forumId));
            return;
        }

        user.addAdminToForum(forumId);
        LOGGER.info(String.format("Admin %d added to forum %d.", userId, forumId));

        userRepository.save(user);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-member-queue}")
    public void removeMemberFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(!user.isAlreadySubscribedToForum(forumId)){
            LOGGER.error(String.format("User %d is not subscribed to forum %d.", userId, forumId));
            return;
        }

        user.getForumsSubscribed().remove(forumId);
        LOGGER.info(String.format("Member %d removed from forum %d.", userId, forumId));

        userRepository.save(user);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-admin-queue}")
    public void removeAdminFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(!user.isAlreadyAdminOfForum(forumId)){
            LOGGER.error(String.format("User %d is not an admin of forum %d.", userId, forumId));
            return;
        }

        user.removeAdminFromForum(forumId);
        LOGGER.info(String.format("Admin %d removed from forum %d.", userId, forumId));

        userRepository.save(user);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.creator-update-queue}")
    public void updateCreator(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(user.getForumsCreated().contains(forumId)){
            LOGGER.error(String.format("User %d is already the creator of forum %d.", userId, forumId));
            return;
        }

        user.getForumsCreated().add(forumId);
        user.getForumsAdmin().add(forumId);
        user.getForumsSubscribed().add(forumId);

        LOGGER.info(String.format("User %d is now the creator of forum %d.", userId, forumId));

        userRepository.save(user);
    }
}
