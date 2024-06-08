package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.repository.ICommentRepository;
import com.classmate.comment_service.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ICommentService interface.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository ICommentRepository;
    private final CommentMapper commentMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    /**
     * Constructs a new CommentServiceImpl with the specified repository and mapper.
     *
     * @param ICommentRepository the comment repository
     * @param commentMapper the comment mapper
     */
    public CommentServiceImpl(ICommentRepository ICommentRepository, CommentMapper commentMapper) {
        this.ICommentRepository = ICommentRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDTO getCommentById(Long id) {
        LOGGER.info("Getting comment by id...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return commentMapper.mapToCommentDTO(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = ICommentRepository.findByPostId(postId, pageable);
        return commentsPage.getContent().stream()
                .map(commentMapper::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDTO saveComment(CommentDTO commentDTO) {
        validateComment(commentDTO.getBody());
        LOGGER.info("Saving comment...");
        Comment comment = commentMapper.mapToComment(commentDTO);
        Comment savedComment = ICommentRepository.save(comment);
        return commentMapper.mapToCommentDTO(savedComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateComment(Long id, CommentUpdateDTO commentUpdateDTO) {
        validateComment(commentUpdateDTO.getBody());
        LOGGER.info("Updating comment...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        comment.setBody(commentUpdateDTO.getBody());
        ICommentRepository.save(comment);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComment(Long id, Long userId) {
        LOGGER.info("Deleting comment...");
        Comment comment = ICommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }
        ICommentRepository.delete(comment);
    }

    /**
     * Validates the comment data.
     *
     * @param body the comment body to validate
     * @throws InvalidCommentException if the comment is invalid
     */
    private void validateComment(String body) {
        if (body == null || body.isEmpty()) {
            throw new InvalidCommentException("Comment body cannot be empty");
        }
        if (body.length() > 2000) {
            throw new InvalidCommentException("Comment body cannot exceed 2000 characters");
        }
    }

}
