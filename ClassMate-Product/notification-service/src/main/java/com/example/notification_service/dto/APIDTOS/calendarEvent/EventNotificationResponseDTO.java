package com.example.notification_service.dto.APIDTOS.calendarEvent;

import com.example.notification_service.dto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventNotificationResponseDTO extends NotificationDTO {
    private String title;
    private LocalDate startDate;

    public EventNotificationResponseDTO(Long id, Long userId, Boolean isSeen, LocalDateTime creationDate,  String title, LocalDate startDate) {
        super(id, userId, isSeen, creationDate, "EVENT");
        this.title = title;
        this.startDate = startDate;
    }
}
