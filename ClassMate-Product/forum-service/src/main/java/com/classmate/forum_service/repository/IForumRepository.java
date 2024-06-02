package com.classmate.forum_service.repository;

import com.classmate.forum_service.entity.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Forum entities.
 */
public interface IForumRepository extends JpaRepository<Forum, Long> {

    /**
     * Finds forums by their title, ignoring case, and orders them by creation date in descending order.
     *
     * @param title the title of the forums to find
     * @param pageable the pagination information
     * @return a page of forums matching the given title
     */
    Page<Forum> findByTitleContainingIgnoreCaseOrderByCreationDateDesc(String title, Pageable pageable);
}
