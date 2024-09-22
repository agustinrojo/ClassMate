package com.classmate.post_service.consumer;

import com.classmate.post_service.dto.notification.PostAuthorRequestEventDTO;
import com.classmate.post_service.dto.notification.PostAuthorResponseEventDTO;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class PostAuthorRequestConsumer {
    Logger LOGGER = LoggerFactory.getLogger(PostAuthorRequestConsumer.class);
    private final IPostRepository postRepository;
    private final PostPublisher postPublisher;

    public PostAuthorRequestConsumer(IPostRepository postRepository, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postPublisher = postPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.post-author-request-queue}")
    public void handlePostAuthorRequest(PostAuthorRequestEventDTO event) {
        Post result = postRepository.findById(event.getPostId())
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: %d not found.", event.getPostId())));

        if (result != null) {
            Long postAuthorId = result.getAuthor().getUserId();
            Long forumId = result.getForumId();
            String title = result.getTitle();


            PostAuthorResponseEventDTO responseEvent = new PostAuthorResponseEventDTO(
                    event.getPostId(),
                    event.getCommentId(),
                    postAuthorId,
                    forumId,
                    title
            );
            postPublisher.publishPostAuthorResponseEvent(responseEvent);
        } else {
            LOGGER.warn("Post authorId not found for handlePostAuthorRequest()");
        }
    }
}
