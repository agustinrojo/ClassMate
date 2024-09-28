package com.classmate.post_service.consumer;


import com.classmate.post_service.dto.notification.GetForumIdNotificationDTORequest;
import com.classmate.post_service.dto.notification.GetForumIdNotificationDTOResponse;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class getForumIdConsumer {
    Logger LOGGER = LoggerFactory.getLogger(PostAuthorRequestConsumer.class);

    private final IPostRepository postRepository;
    private final PostPublisher postPublisher;

    public getForumIdConsumer(IPostRepository postRepository, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postPublisher = postPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.get.forum.id-queue}")
    private void handleGetForumIdNotificationEvent(GetForumIdNotificationDTORequest event) {
        Post result = postRepository.findById(event.getPostId())
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: %d not found.", event.getPostId())));

        GetForumIdNotificationDTOResponse eventResponse = new GetForumIdNotificationDTOResponse(result.getId(), result.getForumId());
        postPublisher.publishGetForumIdResponseEvent(eventResponse);
    }
}
