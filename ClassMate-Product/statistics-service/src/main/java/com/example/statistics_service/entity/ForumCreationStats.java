package com.example.statistics_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "forum_creation_stats")
@Getter
@Setter
@NoArgsConstructor
public class ForumCreationStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime yearMonth;  // To store month and year (e.g., 2024-12)

    @Column(nullable = false)
    private Long forumCount = 0L;  // Count of forums created in that month

    public ForumCreationStats(LocalDateTime yearMonth, Long forumCount) {
        this.yearMonth = yearMonth;
        this.forumCount = forumCount;
    }
}

