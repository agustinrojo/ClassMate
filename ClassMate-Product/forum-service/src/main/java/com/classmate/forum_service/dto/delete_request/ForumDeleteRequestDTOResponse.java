package com.classmate.forum_service.dto.delete_request;

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
public class ForumDeleteRequestDTOResponse {
    /**
     * The unique identifier of the forum.
     */
    private Long id;

    /**
     * The title of the forum.
     */
    private String title;

    /**
     * The description of the forum.
     */

    private Long creatorId;

    private String description;

    private LocalDateTime creationDate;

    private Boolean hasBeenEdited;

    private List<DeleteRequestDTOResponse> deleteRequests;
}
