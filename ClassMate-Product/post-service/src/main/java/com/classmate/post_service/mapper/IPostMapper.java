package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.PostDTO;
import com.classmate.post_service.entity.Post;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Post entity and PostDTO.
 */
@Mapper(componentModel = "spring")
public interface IPostMapper {

    /**
     * Converts a Post entity to a PostDTO.
     *
     * @param post the Post entity
     * @return the PostDTO
     */
    PostDTO convertToPostDTO(Post post);

    /**
     * Converts a PostDTO to a Post entity.
     *
     * @param postDTO the PostDTO
     * @return the Post entity
     */
    Post convertToPost(PostDTO postDTO);

    /**
     * Converts a Post entity to an APIResponseDTO.
     *
     * @param post the Post entity
     * @return the APIResponseDTO
     */
    APIResponseDTO convertToAPIResponseDTO(Post post);
}
