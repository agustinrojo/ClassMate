package com.example.notification_service.dto.comment;


import com.example.notification_service.dto.NotificationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentNotificationResponseDTO extends NotificationDTO {
    private Long postId;
    private Long commentId;
    private Long forumId; // Post's forumId, for forming the URL in the front end (e.g. http://localhost:4200/forum/658/post/909)
    private String title;

    public CommentNotificationResponseDTO(Long id, Long userId, Boolean isSeen, LocalDateTime creationDate, Long postId, Long commentId, Long forumId, String title) {
        super(id, userId, isSeen, creationDate, "COMMENT");
        this.postId = postId;
        this.commentId = commentId;
        this.forumId = forumId;
        this.title = title;
    }


}
