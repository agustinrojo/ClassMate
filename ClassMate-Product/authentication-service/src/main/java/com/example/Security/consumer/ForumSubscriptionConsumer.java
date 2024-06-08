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

        try {
            user.subscribeToForum(forumId);
            userRepository.save(user);
            LOGGER.info(String.format("User %d subscribed to forum %d.", userId, forumId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.add-admin-queue}")
    public void addAdminToForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        try {
            user.addAdminToForum(forumId);
            userRepository.save(user);
            LOGGER.info(String.format("Admin %d added to forum %d.", userId, forumId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-member-queue}")
    public void removeMemberFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        try {
            user.removeSubscriptionFromForum(forumId);
            userRepository.save(user);
            LOGGER.info(String.format("Member %d removed from forum %d.", userId, forumId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-admin-queue}")
    public void removeAdminFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        try {
            user.removeAdminFromForum(forumId);
            userRepository.save(user);
            LOGGER.info(String.format("Admin %d removed from forum %d.", userId, forumId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.creator-update-queue}")
    public void updateCreator(ForumSubscriptionDTO forumSubscriptionDTO) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        try {
            user.addForumAsCreator(forumId);
            userRepository.save(user);
            LOGGER.info(String.format("User %d is now the creator of forum %d.", userId, forumId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
