package com.example.notification_service.entity.notification;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentNotification extends Notification {

    @Column(nullable = false)
    private Long postId; // Post commented on

    @Column(nullable = false)
    private Long commentId; // Comment that triggered the notification

    @Column(nullable = false)
    private Long forumId; // Post's forumId, for forming the URL in the front end (e.g. http://localhost:4200/forum/658/post/909)

    @Column(nullable = true)
    private String title;

    @Override
    public String getType() {
        return "COMMENT";
    }
}
