package com.example.statistics_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "forum_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType; // 'POST' or 'COMMENT'

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long forumId;

    @Column(nullable = false)
    private int activityCount; // Default is 1
}
