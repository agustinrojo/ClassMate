package com.example.notification_service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public abstract class NotificationDTO {
    private Long id;
    private Long userId;
    private Boolean isSeen;
    private LocalDateTime creationDate;
    private String type; // To distinguish different types of notifications from the front-end

    public NotificationDTO(Long id, Long userId, Boolean isSeen, LocalDateTime creationDate, String type) {
        this.id = id;
        this.userId = userId;
        this.isSeen = isSeen;
        this.creationDate = creationDate;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Someone commented on your post!";
    }
}
