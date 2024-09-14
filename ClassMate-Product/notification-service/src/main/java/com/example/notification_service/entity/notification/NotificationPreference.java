package com.example.notification_service.entity.notification;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean commentNotificationEnabled = true; // Comment on personal post

    @Column(nullable = false)
    private Boolean likeNotificationEnabled = true;

    @Column(nullable = false)
    private Boolean messageNotificationEnabled = true;

    @Column(nullable = false)
    private Boolean eventNotificationEnabled = true;

}
