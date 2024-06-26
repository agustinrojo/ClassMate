package com.classmate.comment_service.service;

public interface ICommentValorationService {
    void upvoteComment(Long commentId, Long userId);
    void downvoteComment(Long commentId, Long userId);
    void removeVoteFromComment(Long commentId, Long userId);
}
