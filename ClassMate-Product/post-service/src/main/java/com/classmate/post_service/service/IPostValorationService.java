package com.classmate.post_service.service;

public interface IPostValorationService {
    void upvotePost(Long postId, Long userId);
    void downvotePost(Long postId, Long userId);
    void removeVoteFromPost(Long postId, Long userId);
}
