package com.classmate.comment_service.dto;

import com.classmate.comment_service.dto.filedtos.FileDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private List<FileDTO> files;

    private boolean likedByUser;

    private boolean dislikedByUser;

    private int valoration;
}