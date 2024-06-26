package com.classmate.post_service.dto;

import com.classmate.post_service.dto.filedtos.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for encapsulating the response of a post along with its comments.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO {

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

    /**
     * A list of CommentDTO objects representing the comments associated with the post.
     */
    private List<CommentDTO> commentDTOS;

    private List<FileDTO> files;

    private boolean likedByUser;

    private boolean dislikedByUser;
}
