package com.classmate.comment_service.consumer;

import com.classmate.comment_service.dto.PostDeletionDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.repository.ICommentRepository;
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
public class PostDeletionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostDeletionConsumer.class);

    private final ICommentRepository commentRepository;

    public PostDeletionConsumer(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.delete-post-queue}")
    public void deleteCommentsByPostId(PostDeletionDTO postDeletionDTO) {
        Long postId = postDeletionDTO.getPostId();
        int batchSize = 100;
        Pageable pageable = PageRequest.of(0, batchSize);
        Page<Comment> commentsPage;

        do {
            commentsPage = commentRepository.findByPostId(postId, pageable);
            commentRepository.deleteAll(commentsPage.getContent());
            pageable = commentsPage.nextPageable(); // Moves to the next page
        } while (commentsPage.hasNext());

        LOGGER.info(String.format("Comments for post %d deleted.", postId));
    }
}
