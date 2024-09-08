package com.classmate.comment_service.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotificationEventDTO {
    /**
     * The ID of the post where the comment was made.
     */
    private Long postId;

    /**
     * The ID of the comment that triggered the notification.
     */
    private Long commentId;

    /**
     * The ID of the user who authored the comment (optional).
     */
    private Long commentAuthorId;
}
