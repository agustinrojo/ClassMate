package com.classmate.post_service.dto;

import com.classmate.post_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.post_service.dto.filedtos.FileDTO;
import com.classmate.post_service.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    private Boolean hasBeenEdited;

    private int valoration;
    
    private boolean likedByUser;

    private boolean dislikedByUser;

    private Long commentCount;

    private List<DeleteRequestDTO> deleteRequests;

    private boolean isReportedByUser;
}
