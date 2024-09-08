package com.example.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateDTO {
    private Long id;
    private Boolean isSeen; // Mark notification as seen/unseen
}
