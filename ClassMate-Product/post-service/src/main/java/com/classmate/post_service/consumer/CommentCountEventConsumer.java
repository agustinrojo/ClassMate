package com.classmate.post_service.consumer;

import com.classmate.post_service.dto.comment_count_event.CommentCountEvent;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.repository.IPostRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentCountEventConsumer {
    private IPostRepository postRepository;

    private Logger LOGGER = LoggerFactory.getLogger(CommentCountEventConsumer.class);

    public CommentCountEventConsumer(IPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.comment-count-event-queue}")
    public void setCommentCount(CommentCountEvent commentCountEvent){
        LOGGER.info(String.format("Comment Count" + commentCountEvent));
        Post commentedPost = postRepository.findById(commentCountEvent.getPostId())
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: %d not found.", commentCountEvent.getPostId())));
        commentedPost.setCommentCount(commentCountEvent.getCommentCount());
        postRepository.save(commentedPost);
    }
}
