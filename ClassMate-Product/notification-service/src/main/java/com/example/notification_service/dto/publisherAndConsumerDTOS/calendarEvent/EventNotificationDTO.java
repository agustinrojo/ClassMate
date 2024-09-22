package com.example.notification_service.dto.publisherAndConsumerDTOS.calendarEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventNotificationDTO {
    private Long userId;
    private String title;
    private LocalDate startDate;
}
