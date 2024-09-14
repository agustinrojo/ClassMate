package com.example.notification_service.entity.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventNotification extends Notification{

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Override
    public String getType() {
        return "EVENT";
    }
}
