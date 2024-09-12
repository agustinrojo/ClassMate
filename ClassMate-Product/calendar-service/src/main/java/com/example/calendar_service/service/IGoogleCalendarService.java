package com.example.calendar_service.service;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;

public interface IGoogleCalendarService {
    void createEvent(String calendarId, EventRequestDTO eventRequestDTO) throws IOException;
    void updateEvent(String calendarId, String eventId, EventUpdateDTO updatedEvent, Long userId) throws IOException;
    void deleteEvent(String calendarId, String eventId, Long userId) throws IOException;
    void uploadAllEvents(Long userId, String calendarId);

}
