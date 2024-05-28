package com.classmate.comment_service.service;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.entity.Comment;

import java.util.List;

public interface ICommentService {
    CommentDTO getCommentById(Long id);
    List<CommentDTO> getCommentsByPostId(Long postId);
    CommentDTO saveComment(CommentDTO commentDTO);
    void updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
}
