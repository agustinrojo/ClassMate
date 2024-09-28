package com.classmate.post_service.dto.notification;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetForumIdNotificationDTOResponse {
    private Long postId;
    private Long forumId;
}
