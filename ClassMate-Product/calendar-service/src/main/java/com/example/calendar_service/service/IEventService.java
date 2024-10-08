package com.example.calendar_service.service;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;

import java.util.List;

public interface IEventService {

    List<EventResponseDTO> getEventsByUserId(Long userId);

    EventResponseDTO saveEvent(EventRequestDTO eventRequestDTO);

    void updateEvent(Long eventId, EventUpdateDTO eventUpdateDTO, Long userId);

    void deleteEvent(Long eventId, Long userId);

    void uploadAllEvents(Long userId, String calendarId);
}
