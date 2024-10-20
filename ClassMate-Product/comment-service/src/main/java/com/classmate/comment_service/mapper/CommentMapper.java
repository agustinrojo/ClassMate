package com.classmate.comment_service.mapper;

import com.classmate.comment_service.dto.CommentDTORequest;
import com.classmate.comment_service.dto.CommentDTOResponse;
import com.classmate.comment_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Comment and CommentDTO objects.
 */
@Mapper(componentModel = "spring", uses = AttachmentMapper.class)
public interface CommentMapper {

//    /**
//     * Converts a Comment entity to a CommentDTO.
//     *
//     * @param comment the Comment entity
//     * @return the CommentDTO
//     */
//    CommentDTORequest mapToCommentDTO(Comment comment);

    /**
     * Converts a CommentDTO to a Comment entity.
     *
     * @param commentRequestDTO the CommentDTO
     * @return the Comment entity
     */
    @Mapping(target = "author", ignore = true)
    Comment mapToComment(CommentDTORequest commentRequestDTO);

    @Mapping(source = "attachments", target = "files")
    @Mapping(source = "hasBeenEdited", target = "hasBeenEdited")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "forumId", ignore = true)
    CommentDTOResponse mapToCommentDTOResponse(Comment comment);

}
