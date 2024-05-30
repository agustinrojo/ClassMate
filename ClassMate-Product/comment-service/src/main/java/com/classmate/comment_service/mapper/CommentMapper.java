package com.classmate.comment_service.mapper;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.entity.Comment;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Comment and CommentDTO objects.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Converts a Comment entity to a CommentDTO.
     *
     * @param comment the Comment entity
     * @return the CommentDTO
     */
    CommentDTO mapToCommentDTO(Comment comment);

    /**
     * Converts a CommentDTO to a Comment entity.
     *
     * @param commentDTO the CommentDTO
     * @return the Comment entity
     */
    Comment mapToComment(CommentDTO commentDTO);
}
