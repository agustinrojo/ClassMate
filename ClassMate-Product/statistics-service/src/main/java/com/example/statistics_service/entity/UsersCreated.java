package com.example.statistics_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_created_stats")
@Getter
@Setter
@NoArgsConstructor
public class UsersCreated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usersCreatedCount = 0L;
}
