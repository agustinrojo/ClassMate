package com.classmate.comment_service.consumer;

import com.classmate.comment_service.dto.notifications.GetForumIdNotificationDTOResponse;
import com.classmate.comment_service.dto.notifications.MilestoneReachedEventDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.publisher.CommentPublisher;
import com.classmate.comment_service.repository.ICommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForumIdResponseConsumer {

    private final ICommentRepository commentRepository;
    private final CommentPublisher commentPublisher;
    private final Logger LOGGER = LoggerFactory.getLogger(ForumIdResponseConsumer.class);

    public ForumIdResponseConsumer(ICommentRepository commentRepository, CommentPublisher commentPublisher) {
        this.commentRepository = commentRepository;
        this.commentPublisher = commentPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.get.forum.id.response-queue}")
    public void handleGetForumIdResponse(GetForumIdNotificationDTOResponse response) {
        LOGGER.info("Received forum ID for post: " + response.getPostId() + ", forumId: " + response.getForumId());

        // Now you can find the comment, complete the event and publish the milestone
        Optional<Comment> commentOpt = commentRepository.findById(response.getCommentId());
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();

            // Create the event DTO with the received forumId
            MilestoneReachedEventDTO event = MilestoneReachedEventDTO.builder()
                    .postId(comment.getId())
                    .authorId(comment.getAuthor().getUserId())
                    .forumId(response.getForumId())  // Use the received forumId
                    .milestone(comment.getLastMilestone())  // Assuming the last milestone is already updated
                    .milestoneType("COMMENT")
                    .build();

            LOGGER.info("Publishing milestone reached event for comment with id -> {}", event.getPostId());

            // Publish the event to the notifications service
            commentPublisher.publishMilestoneReachedEvent(event);
        } else {
            LOGGER.error("Comment not found for post id: " + response.getPostId());
        }
    }
}
