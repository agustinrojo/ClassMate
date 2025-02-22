package com.classmate.comment_service.dto;

import com.classmate.comment_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.comment_service.dto.filedtos.FileDTO;
import com.classmate.comment_service.dto.user.UserDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOResponse {

    /**
     * The unique identifier of the comment.
     */
    private Long id;

    /**
     * The identifier of the post to which the comment belongs.
     */
    private Long postId;

    private Long forumId;

    /**
     * The identifier of the author who created the comment.
     */
    private UserDTO author;

    /**
     * The body content of the comment.
     */
    private String body;

    /**
     * The creation date and time of the comment.
     * This field is automatically populated.
     */
    private LocalDateTime creationDate;

    private List<FileDTO> files;

    private Boolean hasBeenEdited;

    private boolean likedByUser;

    private boolean dislikedByUser;

    private int valoration;

    private boolean isReportedByUser;
}