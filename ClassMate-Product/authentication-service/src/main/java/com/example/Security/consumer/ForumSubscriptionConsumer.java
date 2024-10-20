package com.example.Security.consumer;

import com.example.Security.dto.forum.ForumDeletionDTO;
import com.example.Security.dto.forum.ForumSubscriptionDTO;
import com.example.Security.dto.user.BanUserDeleteMemberEventDTO;
import com.example.Security.entities.User;
import com.example.Security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ForumSubscriptionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumSubscriptionConsumer.class);

    private final UserRepository userRepository;

    public ForumSubscriptionConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Ban members
    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.ban-user-delete-member-queue}")
    public void handleBanUserDeleteMemberEvent(BanUserDeleteMemberEventDTO banUserDeleteMemberEventDTO) {
        LOGGER.info("Banning user event");

        // Fetch the user by ID
        User user = userRepository.findById(banUserDeleteMemberEventDTO.getUserIdToBan())
                .orElseThrow(() -> new RuntimeException(String.format("User to ban not found with ID: %s", banUserDeleteMemberEventDTO.getUserIdToBan())));

        Long forumId = banUserDeleteMemberEventDTO.getForumId();

        // Check if the user is an admin of the forum before removing them
        if (user.isAlreadyAdminOfForum(forumId)) {
            user.removeAdminFromForum(forumId);
            LOGGER.info("User {} removed as admin from forum {}", user.getId(), forumId);
        } else {
            LOGGER.warn("User {} is not an admin of forum {}", user.getId(), forumId);
        }

        // Check if the user is subscribed to the forum before removing them
        if (user.isAlreadySubscribedToForum(forumId)) {
            user.removeSubscriptionFromForum(forumId);
            LOGGER.info("User {} unsubscribed from forum {}", user.getId(), forumId);
        } else {
            LOGGER.warn("User {} is not subscribed to forum {}", user.getId(), forumId);
        }
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

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.delete-forum-subscription-queue}")
    public void handleForumSubscriptionDeletion(ForumDeletionDTO forumDeletionDTO){
        Long deletedForumId = forumDeletionDTO.getForumId();
        LOGGER.info(String.format("Removing forums subscriptions of forum: '%d'", deletedForumId));
        handleForumDeletionFromSubscribedUsers(deletedForumId);
        handleForumDeletionFromCreatorUser(deletedForumId);
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

    private void handleForumDeletionFromSubscribedUsers(Long deletedForumId){
        List<User> usersSubscribed = this.userRepository.findByForumsSubscribedContaining(deletedForumId);
        usersSubscribed
                .forEach(u -> u.removeForumSubscription(deletedForumId));
        userRepository.saveAll(usersSubscribed);
    }

    private void handleForumDeletionFromCreatorUser(Long deletedForumId){
        List<User> forumCreatorsUsers = this.userRepository.findByForumsCreatedContaining(deletedForumId);

        forumCreatorsUsers
                .forEach(u -> {
                    u.removeForumCreated(deletedForumId);

                });
        userRepository.saveAll(forumCreatorsUsers);
    }

}
