package com.classmate.comment_service.dto.notifications;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetForumIdNotificationDTOResponse {
    private Long commentId;
    private Long postId;
    private Long forumId;
}
