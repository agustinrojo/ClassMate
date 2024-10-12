package com.classmate.forum_service.mapper;

import com.classmate.forum_service.dto.APIResponseDTO;
import com.classmate.forum_service.dto.ForumResponseDTO;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.entity.Forum;
import com.classmate.forum_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Forum entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = IUserMapper.class)
public interface IForumMapper {

    // Convierte Forum a ForumResponseDTO
    @Mapping(source = "hasBeenEdited", target = "hasBeenEdited")
    ForumResponseDTO convertToForumResponseDTO(Forum forum);

    // Convierte ForumRequestDTO a Forum
    Forum convertToForum(ForumRequestDTO forumRequestDTO);

    // Convierte Forum a APIResponseDTO
    APIResponseDTO convertToAPIResponseDTO(Forum forum);
}
