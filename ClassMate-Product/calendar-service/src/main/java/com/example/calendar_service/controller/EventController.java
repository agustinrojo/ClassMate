package com.example.calendar_service.controller;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.dto.EventUpdateDTO;
import com.example.calendar_service.service.IEventService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventEntities")
public class EventController {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

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
    public ResponseEntity<Void> updateEvent(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody EventUpdateDTO eventUpdateDTO) {
        Long userId = getUserId(authorizationHeader);
        eventService.updateEvent(id, eventUpdateDTO, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, @RequestParam Long userId) {
        eventService.deleteEvent(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getUserId(String authorizationHeader){
        String token = authorizationHeader.substring(7); // Eliminar "Bearer " del encabezado

        // Decodificar el token
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }
}
