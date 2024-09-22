package com.example.calendar_service.service.impl;


import com.example.calendar_service.dto.EventNotificationDTO;
import com.example.calendar_service.entity.calendar.Event;
import com.example.calendar_service.publisher.EventNotificationPublisher;
import com.example.calendar_service.repository.IEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventScheduler {
    private final IEventRepository eventRepository;
    private final EventNotificationPublisher notificationPublisher;

    public EventScheduler(IEventRepository eventRepository, EventNotificationPublisher notificationPublisher) {
        this.eventRepository = eventRepository;
        this.notificationPublisher = notificationPublisher;
    }

    // Method runs every day at 00 AM
    // secon, minute, hour, dayOfMonth, month, dayOfWeek
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkForTodayEvents() {
        LocalDate today = LocalDate.now();
        List<Event> todayEvents = eventRepository.findByStartDate(today);

        for(Event event : todayEvents) {
            // Publish a notification for each event
            EventNotificationDTO eventNotificationDTO = new EventNotificationDTO(
                    event.getUserId(),
                    event.getTitle(),
                    event.getStartDate()
            );
            notificationPublisher.publishEventNotification(eventNotificationDTO);
        }
    }
}
