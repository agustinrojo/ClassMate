package com.classmate.comment_service.dto.notifications;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetForumIdNotificationDTORequest {
    private Long commentId;
    private Long postId;
}
