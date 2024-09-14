package com.example.notification_service.dto.publisherAndConsumerDTOS.comment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAuthorRequestEventDTO {

    private Long postId;
    private Long commentId;
}
