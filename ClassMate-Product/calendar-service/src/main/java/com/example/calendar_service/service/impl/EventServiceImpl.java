package com.example.calendar_service.service.impl;

import com.example.calendar_service.dto.EventNotificationDTO;
import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;
import com.example.calendar_service.entity.calendar.Event;
import com.example.calendar_service.exception.EventNotFoundException;
import com.example.calendar_service.exception.UnauthorizedActionException;
import com.example.calendar_service.mapper.IEventMapper;
import com.example.calendar_service.publisher.EventNotificationPublisher;
import com.example.calendar_service.repository.IEventRepository;
import com.example.calendar_service.repository.ITokenRepository;
import com.example.calendar_service.service.IEventService;
import com.example.calendar_service.service.IGoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final IEventMapper eventMapper;
    private final IGoogleCalendarService googleCalendarService;
    private final ITokenRepository tokenRepository;
    private final EventNotificationPublisher notificationPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    public EventServiceImpl(IEventRepository eventRepository, IEventMapper eventMapper, IGoogleCalendarService googleCalendarService, ITokenRepository tokenRepository, EventNotificationPublisher notificationPublisher) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.googleCalendarService = googleCalendarService;
        this.tokenRepository = tokenRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public List<EventResponseDTO> getEventsByUserId(Long userId) {
        LOGGER.info("Getting events by user with id: {}", userId);
        List<Event> eventEntities = eventRepository.findEventsByUserId(userId);

        if (eventEntities == null || eventEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return eventEntities.stream().map(eventMapper::mapToResponseEventDTO).collect(Collectors.toList());
    }

    @Override
    public EventResponseDTO saveEvent(EventRequestDTO eventRequestDTO) {
        LOGGER.info("Saving event with id: {}", eventRequestDTO.getId());

        Event eventEntity = eventMapper.mapToEvent(eventRequestDTO);
        Event savedEventEntity = eventRepository.save(eventEntity);

        eventRequestDTO.setId(savedEventEntity.getId());

        if(tokenRepository.existsByUserId(eventRequestDTO.getUserId())){
            try {
                googleCalendarService.createEvent("primary",eventRequestDTO );
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        // Push notification if events date is today
        pushEventNotification(savedEventEntity);

        return eventMapper.mapToResponseEventDTO(savedEventEntity);
    }



    @Override
    public void updateEvent(Long eventId, EventUpdateDTO eventUpdateDTO, Long userId) {
        LOGGER.info("Updating event with id: {}", eventId);

        Event eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        eventEntity.setTitle(eventUpdateDTO.getTitle());
        eventEntity.setDescription(eventUpdateDTO.getDescription());
        eventEntity.setStartDate(eventUpdateDTO.getStartDate());
        eventEntity.setEndDate(eventUpdateDTO.getEndDate());

        Event savedEvent = eventRepository.save(eventEntity);

        pushEventNotification(savedEvent);

        if(tokenRepository.existsByUserId(userId)){
            if(eventEntity.getGoogleId() != null){
                try {
                    googleCalendarService.updateEvent("primary", eventEntity.getGoogleId(), eventUpdateDTO, userId);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void pushEventNotification(Event event) {
        LocalDate today = LocalDate.now();
        if (event.getStartDate().equals(today)) {
            EventNotificationDTO eventNotificationDTO = new EventNotificationDTO(
                    event.getUserId(),
                    event.getTitle(),
                    event.getStartDate()
            );
            notificationPublisher.publishEventNotification(eventNotificationDTO);
        }
    }

    @Override
    public void deleteEvent(Long eventId, Long userId) {
        LOGGER.info("Deleting event with id: {}", eventId);
        Event eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        if (!eventEntity.getUserId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this comment");
        }

        eventRepository.delete(eventEntity);

        if(tokenRepository.existsByUserId(userId)){
            if(eventEntity.getGoogleId() != null){
                try {
                    googleCalendarService.deleteEvent("primary", eventEntity.getGoogleId(), userId);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void uploadAllEvents(Long userId, String calendarId) {
        googleCalendarService.uploadAllEvents(userId, calendarId);
    }
}
