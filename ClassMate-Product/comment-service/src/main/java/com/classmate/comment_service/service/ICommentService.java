package com.classmate.comment_service.service;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.dto.CommentUpdateDTO;

import java.util.List;

/**
 * Service interface for managing comments.
 */
public interface ICommentService {

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the ID of the comment
     * @return the comment DTO
     */
    CommentDTO getCommentById(Long id);

    /**
     * Retrieves a page of comments by post ID.
     *
     * @param postId the ID of the post
     * @param page the page number
     * @param size the page size
     * @return a list of comment DTOs
     */
    List<CommentDTO> getCommentsByPostId(Long postId, int page, int size);

    /**
     * Saves a new comment.
     *
     * @param commentDTO the comment DTO
     * @return the saved comment DTO
     */
    CommentDTO saveComment(CommentDTO commentDTO);

    /**
     * Updates an existing comment.
     *
     * @param id the ID of the comment to update
     * @param commentDTO the comment DTO
     */
    void updateComment(Long id, CommentUpdateDTO commentUpdateDTO);

    /**
     * Deletes a comment by its ID and user ID.
     *
     * @param id the ID of the comment to delete
     * @param userId the ID of the user performing the deletion
     */
    void deleteComment(Long id, Long userId);
}
