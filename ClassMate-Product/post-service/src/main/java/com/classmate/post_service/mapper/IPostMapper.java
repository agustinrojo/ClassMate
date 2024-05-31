package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.PostDTO;
import com.classmate.post_service.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPostMapper {

    PostDTO convertToPostDTO(Post post);

    Post convertToPost(PostDTO postDTO);

    APIResponseDTO convertToAPIResponseDTO(Post post);
}
