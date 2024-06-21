package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.PostDTO;
import com.classmate.post_service.dto.PostRequestDTO;
import com.classmate.post_service.dto.PostResponseDTO;
import com.classmate.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Post entity and PostDTO.
 */
@Mapper(componentModel = "spring", uses = AttachmentMapper.class)
public interface IPostMapper {


    PostDTO convertToPostDTO(Post post);

    Post mapToPost(PostRequestDTO postRequestDTO);

    @Mapping(source = "attachments", target = "files")
    APIResponseDTO convertToAPIResponseDTO(Post post);

    @Mapping(source = "attachments", target = "files")
    PostResponseDTO convertToPostResponseDTO(Post post);
}
