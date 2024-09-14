package com.classmate.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveDTO {
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
    private List<MultipartFile> files;
}
