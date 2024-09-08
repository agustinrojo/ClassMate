package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Get all notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notificationDTOS = service.getUserNotifications(userId);
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }

    // Update a notification's status (mark as seen/unseen)
    @PutMapping("/update")
    public ResponseEntity<Void> updateNotification(@RequestBody NotificationUpdateDTO updateDTO) {
        service.updateNotification(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
