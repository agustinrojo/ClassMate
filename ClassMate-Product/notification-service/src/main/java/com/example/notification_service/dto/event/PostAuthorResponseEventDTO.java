package com.example.notification_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAuthorResponseEventDTO {

    private Long postId;
    private Long commentId;
    private Long postAuthorId;
}
