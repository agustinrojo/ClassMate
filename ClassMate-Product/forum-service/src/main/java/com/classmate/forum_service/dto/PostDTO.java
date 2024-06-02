package com.classmate.forum_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for the Post entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    /**
     * The unique identifier of the post.
     */
    private Long id;

    /**
     * The identifier of the forum to which the post belongs.
     */
    private Long forumId;

    /**
     * The identifier of the author who created the post.
     */
    private Long authorId;

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The body content of the post.
     */
    private String body;

    /**
     * The creation date and time of the post.
     * This field is automatically populated.
     */
    private LocalDateTime creationDate;
}
