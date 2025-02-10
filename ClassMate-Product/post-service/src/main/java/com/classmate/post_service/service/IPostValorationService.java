package com.classmate.post_service.service;

import com.classmate.post_service.dto.delete_request.PostDeleteRequestDTO;

import java.util.List;

public interface IPostValorationService {
    void upvotePost(Long postId, Long userId);
    void downvotePost(Long postId, Long userId);
    void removeVoteFromPost(Long postId, Long userId);

}
