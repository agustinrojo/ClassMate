package com.example.Security.consumer;

import com.example.Security.dto.post.CreatePostEvent;
import com.example.Security.dto.post.PostDeletionDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostEventsConsumer {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostEventsConsumer.class);

    public PostEventsConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.create-post-queue}")
    public void handlePostCreatedEvent(CreatePostEvent createPostEvent){
        LOGGER.info("Processing createPostEvent: " + createPostEvent);
        Long userId = createPostEvent.getUserId();
        Long postId = createPostEvent.getPostId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));
        user.addPostCreated(postId);
        userRepository.save(user);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.delete-user-post-queue}")
    public void handlePostDeletedEvent(PostDeletionDTO postDeletionDTO){
        System.out.println("Processing postDeletedEvent: " + postDeletionDTO);

        List<User> users = userRepository.findByPostsCreatedContaining(postDeletionDTO.getPostId());
        if(users.isEmpty()){
            return;
        }
        users.forEach((User u) -> u.removePostCreated(postDeletionDTO.getPostId()));
        userRepository.saveAll(users);
    }
}
