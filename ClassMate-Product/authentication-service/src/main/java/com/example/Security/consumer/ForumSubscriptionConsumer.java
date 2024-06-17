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

import java.util.Optional;

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
    public void subscribeToForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        handleSubscription(forumSubscriptionDTO, this::subscribeToForumInternal);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.add-admin-queue}")
    public void addAdminToForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        handleSubscription(forumSubscriptionDTO, this::addAdminToForumInternal);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-member-queue}")
    public void removeMemberFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        handleSubscription(forumSubscriptionDTO, this::removeMemberFromForumInternal);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.remove-admin-queue}")
    public void removeAdminFromForum(ForumSubscriptionDTO forumSubscriptionDTO) {
        handleSubscription(forumSubscriptionDTO, this::removeAdminFromForumInternal);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.creator-update-queue}")
    public void updateCreator(ForumSubscriptionDTO forumSubscriptionDTO) {
        handleSubscription(forumSubscriptionDTO, this::updateCreatorInternal);
    }

    private void handleSubscription(ForumSubscriptionDTO forumSubscriptionDTO, SubscriptionHandler handler) {
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            LOGGER.error(String.format("User %d not found while processing forum %d.", userId, forumId));
            return;
        }

        User user = optionalUser.get();

        try {
            handler.handle(user, forumId);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void subscribeToForumInternal(User user, Long forumId) {
        user.subscribeToForum(forumId);
        LOGGER.info(String.format("User %d subscribed to forum %d.", user.getId(), forumId));
    }

    private void addAdminToForumInternal(User user, Long forumId) {
        user.addAdminToForum(forumId);
        LOGGER.info(String.format("Admin %d added to forum %d.", user.getId(), forumId));
    }

    private void removeMemberFromForumInternal(User user, Long forumId) {
        user.removeSubscriptionFromForum(forumId);
        LOGGER.info(String.format("Member %d removed from forum %d.", user.getId(), forumId));
    }

    private void removeAdminFromForumInternal(User user, Long forumId) {
        user.removeAdminFromForum(forumId);
        LOGGER.info(String.format("Admin %d removed from forum %d.", user.getId(), forumId));
    }

    private void updateCreatorInternal(User user, Long forumId) {
        user.addForumAsCreator(forumId);
        LOGGER.info(String.format("User %d is now the creator of forum %d.", user.getId(), forumId));
    }

    @FunctionalInterface
    private interface SubscriptionHandler {
        void handle(User user, Long forumId);
    }
}
