package com.example.notification_service.dto.message;


import com.example.notification_service.dto.NotificationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageNotificationResponseDTO extends NotificationDTO {
    private Long senderId;
    private String senderName;

    public MessageNotificationResponseDTO(Long id, Long userId, Boolean isSeen, LocalDateTime creationDate, Long senderId, String senderName) {
        super(id, userId, isSeen, creationDate, "MESSAGE");
        this.senderId = senderId;
        this.senderName = senderName;
    }
}
