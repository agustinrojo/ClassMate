package com.example.calendar_service.controller;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;
import com.example.calendar_service.service.IEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/userEvents/{userId}")
    public ResponseEntity<List<EventResponseDTO>> getEventsByUserId(@PathVariable Long userId) {
        List<EventResponseDTO> userEvents = eventService.getEventsByUserId(userId);
        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> saveEvent(@RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO savedEvent = eventService.saveEvent(eventRequestDTO);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @RequestBody EventUpdateDTO eventUpdateDTO) {
        eventService.updateEvent(id, eventUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, @RequestParam Long userId) {
        eventService.deleteEvent(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
