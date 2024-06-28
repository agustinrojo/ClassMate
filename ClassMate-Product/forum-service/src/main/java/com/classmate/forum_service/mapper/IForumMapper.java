package com.classmate.forum_service.mapper;

import com.classmate.forum_service.dto.APIResponseDTO;
import com.classmate.forum_service.dto.ForumResponseDTO;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.entity.Forum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Forum entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface IForumMapper {

    /**
     * Converts a Forum entity to a ForumResponseDTO.
     *
     * @param forum the Forum entity
     * @return the ForumResponseDTO
     */
    @Mapping(source = "hasBeenEdited", target = "hasBeenEdited")
    ForumResponseDTO convertToForumResponseDTO(Forum forum);

    /**
     * Converts a ForumRequestDTO to a Forum entity.
     *
     * @param forumRequestDTO the ForumRequestDTO
     * @return the Forum entity
     */
    Forum convertToForum(ForumRequestDTO forumRequestDTO);

    /**
     * Converts a Forum entity to an APIResponseDTO.
     *
     * @param forum the Forum entity
     * @return the APIResponseDTO
     */
    APIResponseDTO convertToAPIResponseDTO(Forum forum);

}
