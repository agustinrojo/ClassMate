package com.classmate.post_service.service;

import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.PostDTO;

import java.util.List;

public interface IPostService {
    APIResponseDTO getPostById(Long id);
    List<PostDTO> getPostsByName(String name, int page, int size);
    List<PostDTO> getPostsByForumId(Long forumId, int page, int size);
    PostDTO savePost(PostDTO postDTO);
    void updatePost(Long id, PostDTO postDTO);
    void deletePost(Long id, Long userId);
}
