package com.example.notification_service.dto.publisherAndConsumerDTOS.comment;


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
    private Long forumId; // Post's forumId, for forming the URL in the front end (e.g. http://localhost:4200/forum/658/post/909)
    private String title;
}
