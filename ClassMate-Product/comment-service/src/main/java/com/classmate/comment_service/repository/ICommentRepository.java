package com.classmate.comment_service.repository;

import com.classmate.comment_service.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing Comment entities.
 */
public interface ICommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds a page of comments by post ID.
     *
     * @param postId the ID of the post
     * @param pageable the pagination information
     * @return a page of comments
     */
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    long countByPostId(Long postId);
    @Query("SELECT c FROM Comment c WHERE SIZE(c.deleteRequests) > 0")
    Page<Comment> findAllWithDeleteRequests(Pageable pageable);
}
