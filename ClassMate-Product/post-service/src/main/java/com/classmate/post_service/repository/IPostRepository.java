package com.classmate.post_service.repository;

import com.classmate.post_service.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByForumId(Long forumId, Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCase(String name, Pageable pageable);
}
