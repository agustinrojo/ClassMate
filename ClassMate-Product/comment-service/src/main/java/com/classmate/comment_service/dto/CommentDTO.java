package com.classmate.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

/**
 * Data Transfer Object for Comment entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * The unique identifier of the comment.
     */
    private Long id;

    /**
     * The identifier of the post to which the comment belongs.
     */
    private Long postId;

    /**
     * The identifier of the author who created the comment.
     */
    private Long authorId;

    /**
     * The body content of the comment.
     */
    private String body;

    /**
     * The creation date and time of the comment.
     * This field is automatically populated.
     */
    private LocalDateTime creationDate;
}
