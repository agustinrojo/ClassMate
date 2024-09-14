package com.example.notification_service.dto.publisherAndConsumerDTOS.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageSenderNameRequestDTO {
    private Long receiverId;
    private Long senderId;
}
