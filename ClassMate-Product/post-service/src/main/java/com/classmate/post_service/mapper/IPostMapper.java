package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.*;
import com.classmate.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Post entity and PostDTO.
 */
@Mapper(componentModel = "spring", uses = {AttachmentMapper.class, DeleteRequestMapper.class})
public interface IPostMapper {


    PostDTO convertToPostDTO(Post post);

//    Post mapToPost(PostRequestDTO postRequestDTO);

    Post mapToPost(PostSaveDTO postSaveDTO);

    @Mapping(source = "attachments", target = "files")
    @Mapping(target = "author", ignore = true)
    APIResponseDTO convertToAPIResponseDTO(Post post);

    @Mapping(source = "attachments", target = "files")
    @Mapping(target = "author", ignore = true)
    PostResponseDTO convertToPostResponseDTO(Post post);
}
