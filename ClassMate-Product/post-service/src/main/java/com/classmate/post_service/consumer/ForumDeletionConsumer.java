package com.classmate.post_service.consumer;

import com.classmate.post_service.dto.ForumDeletionDTO;
import com.classmate.post_service.dto.PostDeletionDTO;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForumDeletionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumDeletionConsumer.class);

    private final IPostRepository postRepository;
    private final PostPublisher postPublisher;

    public ForumDeletionConsumer(IPostRepository postRepository, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postPublisher = postPublisher;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.delete-forum-queue}")
    public void deletePostsByForumId(ForumDeletionDTO forumDeletionDTO) {
        Long forumId = forumDeletionDTO.getForumId();
        int batchSize = 100;
        Pageable pageable = PageRequest.of(0, batchSize);
        Page<Post> postsPage;

        do {
            postsPage = postRepository.findByForumId(forumId, pageable);
            postsPage.getContent().forEach(post -> {
                postRepository.delete(post);
                // Publish post deletion event for each post
                PostDeletionDTO postDeletionDTO = new PostDeletionDTO(post.getId());
                postPublisher.publishPostDeletion(postDeletionDTO);
            });
            pageable = postsPage.nextPageable();
        } while (postsPage.hasNext());

        LOGGER.info(String.format("Posts for forum %d deleted.", forumId));
    }
}
