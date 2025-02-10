package com.classmate.post_service.dto.delete_request;

import com.classmate.post_service.dto.filedtos.FileDTO;
import com.classmate.post_service.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDeleteRequestDTO {
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

    private Long commentCount;

    private List<DeleteRequestDTOResponse> deleteRequests;

}
