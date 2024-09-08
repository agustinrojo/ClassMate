package com.example.notification_service.dto.event.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageSenderNameResponseDTO {
    private Long receiverId;
    private Long senderId;
    private String profileName;
}
