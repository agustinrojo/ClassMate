package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationDTO;
import com.example.notification_service.dto.NotificationUpdateDTO;
import com.example.notification_service.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    public ResponseEntity<Page<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationDTOS = service.getUserNotifications(userId, pageable);
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }


    // Update a notification's status (mark as seen/unseen)
    @PutMapping("/update")
    public ResponseEntity<Void> updateNotification(@RequestBody NotificationUpdateDTO updateDTO) {
        service.updateNotification(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
