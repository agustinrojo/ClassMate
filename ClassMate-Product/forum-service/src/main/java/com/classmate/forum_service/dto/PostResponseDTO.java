package com.classmate.forum_service.dto;

import com.classmate.forum_service.dto.file.FileDTO;
import com.classmate.forum_service.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for the Post entity.
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

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
    private UserDTO author;

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

    private List<FileDTO> files;

    private boolean hasBeenEdited;

    private int valoration;

    private boolean likedByUser;

    private boolean dislikedByUser;

    private Long commentCount;
}
