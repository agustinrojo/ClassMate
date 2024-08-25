package com.example.calendar_service.service.impl;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;
import com.example.calendar_service.entity.Event;
import com.example.calendar_service.mapper.IEventMapper;
import com.example.calendar_service.repository.IEventRepository;
import com.example.calendar_service.service.IEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final IEventMapper eventMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    public EventServiceImpl(IEventRepository eventRepository, IEventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }


    @Override
    public List<EventResponseDTO> getEventsByUserId(Long userId) {
        LOGGER.info("Getting events by user with id: {}", userId);
        List<Event> events = eventRepository.findEventsByUserId(userId);

        return events.stream().map(eventMapper::mapToResponseEventDTO).collect(Collectors.toList());
    }

    @Override
    public EventResponseDTO saveEvent(EventRequestDTO eventRequestDTO) {
        LOGGER.info("Saving event with id: {}", eventRequestDTO.getId());

        Event event = eventMapper.mapToEvent(eventRequestDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.mapToResponseEventDTO(savedEvent);
    }

    @Override
    public void updateEvent(Long eventId, EventUpdateDTO eventUpdateDTO) {
        LOGGER.info("Updating event with id: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        event.setTitle(eventUpdateDTO.getTitle());
        event.setDescription(eventUpdateDTO.getDescription());
        event.setStartDate(eventUpdateDTO.getStartDate());
        event.setEndDate(eventUpdateDTO.getEndDate());

        eventRepository.save(event);
    }
}
