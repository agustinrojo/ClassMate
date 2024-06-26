package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.service.ICommentValorationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommentValorationServiceImpl implements ICommentValorationService {

    private final ICommentRepository commentRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(CommentValorationServiceImpl.class);

    public CommentValorationServiceImpl(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void upvoteComment(Long commentId, Long userId) {
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id: '%d' not found",  commentId)));
        comment.addUpvote(userId);
        commentRepository.save(comment);
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
}
