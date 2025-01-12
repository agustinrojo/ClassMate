package com.example.Security.consumer;


import com.example.Security.dto.user.userReputation.ReputationAction;
import com.example.Security.dto.user.userReputation.UserReputationChangeDTO;
import com.example.Security.entities.User;
import com.example.Security.entities.UserReputation;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import com.example.Security.repositories.UserReputationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserReputationConsumer {
    private final UserRepository userRepository;
    private final UserReputationRepository userReputationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserReputationConsumer.class);

    public UserReputationConsumer(UserRepository userRepository, UserReputationRepository userReputationRepository) {
        this.userRepository = userRepository;
        this.userReputationRepository = userReputationRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.user-reputation-queue}")
    public void updateUserReputation(UserReputationChangeDTO event) {
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", event.getUserId()));

        try {
            UserReputation userReputation = user.getReputation();
            if (userReputation == null) {
                userReputation = new UserReputation();
                userReputation.setUser(user);
                user.setReputation(userReputation);
                userReputationRepository.save(userReputation); // Save to avoid null issues
            }

            if (event.getAction() == ReputationAction.LIKE) {
                LOGGER.info(String.format("Publishing UserReputationChange LIKE event for user with id -> %s", event.getUserId()));
                userReputation.addLike();
            } else if(event.getAction() == ReputationAction.DISLIKE) {
                LOGGER.info(String.format("Publishing UserReputationChange DISLIKE event for user with id -> %s", event.getUserId()));
                userReputation.addDislike();
            } else if (event.getAction() == ReputationAction.REMOVELIKE) {
                LOGGER.info(String.format("Publishing UserReputationChange REMOVE LIKE event for user with id -> %s", event.getUserId()));
                userReputation.removeLike();
            } else if (event.getAction() == ReputationAction.REMOVEDISLIKE) {
                LOGGER.info(String.format("Publishing UserReputationChange REMOVE DISLIKE event for user with id -> %s", event.getUserId()));
                userReputation.removeDislike();
            }

            userReputationRepository.save(userReputation);
        } catch (Exception e) {
            LOGGER.error("Failed to update user reputation for user ID: {}", event.getUserId(), e);
            throw new RuntimeException("Error updating user reputation", e);
        }
    }
}
