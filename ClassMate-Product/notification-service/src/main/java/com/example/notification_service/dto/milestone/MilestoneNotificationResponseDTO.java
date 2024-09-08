package com.example.notification_service.dto.milestone;


import com.example.notification_service.dto.NotificationDTO;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MilestoneNotificationResponseDTO extends NotificationDTO {
    private int milestone;
    private Long postId;
    private Long forumId;
    private String title;

    public MilestoneNotificationResponseDTO(Long id, Long userId, Boolean isSeen, LocalDateTime creationDate, int milestone, Long postId, Long forumId, String title) {
        super(id, userId, isSeen, creationDate, "MILESTONE");
        this.milestone = milestone;
        this.postId = postId;
        this.forumId = forumId;
        this.title = title;
    }
}
