package com.classmate.comment_service.dto.notifications;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneReachedEventDTO {
    private Long postId;
    private Long authorId;
    private Long forumId;
    private int milestone;
    private String title;
    private String milestoneType; // COMMENT or POST
}

