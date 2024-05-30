package com.example.Security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String token;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token,User user,LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.token = token;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiredAt;
    }
}
