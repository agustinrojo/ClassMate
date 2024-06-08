package com.classmate.post_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class representing a Post in the forum.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    /**
     * Unique identifier for the Post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Identifier of the forum to which this post belongs.
     */
    @Column(nullable = false)
    private Long forumId;

    /**
     * Identifier of the author who created this post.
     */
    @Column(nullable = false)
    private Long authorId;

    /**
     * Title of the post.
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * Body content of the post.
     */
    @Column(nullable = false, length = 5000)
    private String body;

    // Falta photo, attachments y valuation

    /**
     * Timestamp indicating when the post was created.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;
}
