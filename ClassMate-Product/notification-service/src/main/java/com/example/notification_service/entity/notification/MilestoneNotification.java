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
public class MilestoneNotification extends Notification {

    @Column(nullable = false)
    private int milestone;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long forumId;

    @Column(nullable = true)
    private String title;

    @Column(nullable = false)
    private String milestoneType; // COMMENT or POST

    @Override
    public String getType() {
        return "MILESTONE";
    }
}

