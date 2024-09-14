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
public class MessageNotification extends Notification {

    @Column(nullable = false)
    private Long senderId; // Person who sent the message

    @Column(nullable = false)
    private String senderName;


    @Override
    public String getType() {
        return "MESSAGE";
    }

}
