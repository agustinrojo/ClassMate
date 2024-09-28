package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.dto.notifications.GetForumIdNotificationDTORequest;
import com.classmate.comment_service.dto.notifications.MilestoneReachedEventDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.publisher.CommentPublisher;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.service.ICommentValorationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentValorationServiceImpl implements ICommentValorationService {

    private final ICommentRepository commentRepository;
    private final CommentPublisher commentPublisher;
    private final Logger LOGGER = LoggerFactory.getLogger(CommentValorationServiceImpl.class);
    private static final List<Integer> MILESTONES = List.of(2, 10, 25, 50, 75, 100, 150, 200, 300, 400, 500, 1000);

    public CommentValorationServiceImpl(ICommentRepository commentRepository, CommentPublisher commentPublisher) {
        this.commentRepository = commentRepository;
        this.commentPublisher = commentPublisher;
    }

    @Override
    @Transactional
    public void upvoteComment(Long commentId, Long userId) {
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id: '%d' not found",  commentId)));
        comment.addUpvote(userId);
        commentRepository.save(comment);

        checkForMilestone();
    }

    @Override
    @Transactional
    public void downvoteComment(Long commentId, Long userId) {
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id: '%d' not found",  commentId)));
        comment.addDownvote(userId);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void removeVoteFromComment(Long commentId, Long userId) {
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id: '%d' not found",  commentId)));
        comment.removeVote(userId);
        commentRepository.save(comment);
    }

    private void checkForMilestone(Comment comment) {
        int currentValoration = comment.getValoration();

        // Find the highest milestone reached that is larger than lastMilestone but smaller than currentValoration
        Optional<Integer> milestone = MILESTONES.stream()
                .filter(m -> m > comment.getLastMilestone() && currentValoration >= m)
                .max(Integer::compareTo);

        if (milestone.isPresent()) {
            comment.setLastMilestone(milestone.get());  // Update the last milestone

            // Save the post to persist the milestone change
            commentRepository.save(comment);

            // Publish the event to the notifications service
            this.publishMilestoneReachedEvent(comment, milestone.get());
        }
    }

    private void publishMilestoneReachedEvent(Comment comment, int milestone) {
        // Publish event to receive forumId
        GetForumIdNotificationDTORequest getForumIdEvent = new GetForumIdNotificationDTORequest(comment.getId(), comment.getPostId());
        commentPublisher.publishGetForumIdNotificationEvent(getForumIdEvent);

        LOGGER.info("Published request to get forumId for comment's postId -> {}", comment.getPostId());
    }
}
