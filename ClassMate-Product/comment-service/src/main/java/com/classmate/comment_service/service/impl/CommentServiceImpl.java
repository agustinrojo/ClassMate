package com.classmate.comment_service.service.impl;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.repository.CommentRepository;
import com.classmate.comment_service.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        return commentMapper.mapToCommentDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(commentMapper::mapToCommentDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO saveComment(CommentDTO commentDTO) {
        LOGGER.info("Saving comment...");
        Comment comment = commentMapper.mapToComment(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToCommentDTO(savedComment);
    }

    @Override
    public void updateComment(Long id, CommentDTO commentDTO) {
        LOGGER.info("Updating comment...");
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setId(commentDTO.getId());
        comment.setAuthorId(commentDTO.getAuthorId());
        comment.setBody(commentDTO.getBody());
        comment.setCreationDate(commentDTO.getCreationDate());

        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        LOGGER.info("Deleting comment...");
        commentRepository.deleteById(id);
    }
}
