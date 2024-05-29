package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.CommentNotFoundException;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.repository.CommentRepository;
import com.classmate.comment_service.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);


    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        LOGGER.info("Getting comment by id...");
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return commentMapper.mapToCommentDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);
        return commentsPage.getContent().stream()
                .map(commentMapper::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO saveComment(CommentDTO commentDTO) {
        validateCommment(commentDTO);
        LOGGER.info("Saving comment...");
        Comment comment = commentMapper.mapToComment(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToCommentDTO(savedComment);
    }

    @Override
    public void updateComment(Long id, CommentDTO commentDTO) {
        validateCommment(commentDTO);
        LOGGER.info("Updating comment...");
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        comment.setId(commentDTO.getId());
        comment.setAuthorId(commentDTO.getAuthorId());
        comment.setBody(commentDTO.getBody());
        comment.setCreationDate(commentDTO.getCreationDate());

        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id, Long userId) {
        LOGGER.info("Deleting comment...");
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

    private void validateCommment(CommentDTO commentDTO) {
        if(commentDTO.getBody() == null || commentDTO.getBody().isEmpty()) {
            throw new InvalidCommentException("Comment body cannot be empty");
        }

        if(commentDTO.getBody().length() > 2000) {
            throw new InvalidCommentException("Comment body cannot exceed 2000 characters");
        }
    }

}
