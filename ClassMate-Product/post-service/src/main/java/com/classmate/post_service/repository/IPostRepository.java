package com.classmate.post_service.repository;

import com.classmate.post_service.entity.Post;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Post entities.
 */
public interface IPostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds posts by forum ID with pagination support.
     *
     * @param forumId the forum ID
     * @param pageable the pagination information
     * @return a page of posts belonging to the specified forum
     */
    Page<Post> findByForumId(Long forumId, Pageable pageable);

    /**
     * Finds posts by title containing a specified string, ignoring case, with pagination support.
     * Results are ordered by creation date in descending order.
     *
     * @param name the title string to search for
     * @param pageable the pagination information
     * @return a page of posts with titles containing the specified string
     */
    Page<Post> findByTitleContainingIgnoreCaseOrderByCreationDateDesc(String name, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndForumIdOrderByCreationDateDesc(String name, Long forumId, Pageable pageable);

    Page<Post> findByForumIdInOrderByCreationDateDesc(List<Long> forumIds, Pageable pageable);

    Page<Post> findAllByAuthor_UserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE SIZE(p.deleteRequests) > 0")
    Page<Post> findAllWithDeleteRequests(Pageable pageable);

}
