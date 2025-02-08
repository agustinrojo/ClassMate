package com.classmate.comment_service.service;

import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.dto.CommentUpdateDTO;
import com.classmate.comment_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.comment_service.entity.enums.Role;

import java.io.IOException;
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
    CommentDTOResponse getCommentById(Long id, Long userId);

    /**
     * Retrieves a page of comments by post ID.
     *
     * @param postId the ID of the post
     * @param page the page number
     * @param size the page size
     * @return a list of comment DTOs
     */
    List<CommentDTOResponse> getCommentsByPostId(Long postId, Long userId, int page, int size);

    /**
     * Saves a new comment.
     *
     * @param commentRequestDTO the comment DTO
     * @return the saved comment DTO
     */
    CommentDTOResponse saveComment(CommentDTORequest commentRequestDTO);

    /**
     * Updates an existing comment.
     *
     * @param id the ID of the comment to update
     *  the comment DTO
     */
    void updateComment(Long id, CommentUpdateDTO commentUpdateDTO);

    /**
     * Deletes a comment by its ID and user ID.
     *
     * @param id the ID of the comment to delete
     * @param userId the ID of the user performing the deletion
     */
    void deleteComment(Long id, Long userId, Role role);

    void reportComment(DeleteRequestDTO deleteRequest, Long commentId);

}
