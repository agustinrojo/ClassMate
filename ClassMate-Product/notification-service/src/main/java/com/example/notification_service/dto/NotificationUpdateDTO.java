package com.example.notification_service.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotificationUpdateDTO {
    private Long id;
    private Boolean isSeen; // Mark notification as seen/unseen
}
