package com.example.notification_service.controller;

import com.example.notification_service.dto.preference.NotificationPreferenceUpdateDTO;
import com.example.notification_service.entity.notification.NotificationPreference;
import com.example.notification_service.service.NotificationPreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
public class NotificationPreferenceController {
    private final NotificationPreferenceService preferenceService;


    public NotificationPreferenceController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // Get user preferences
    @GetMapping("/user/{userId}")
    public ResponseEntity<NotificationPreference> getUserPreferences(@PathVariable Long userId) {
        NotificationPreference preferences = preferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    // Update user preferences using DTO
    @PutMapping("/user/{userId}")
    public ResponseEntity<NotificationPreference> updateUserPreferences(
            @PathVariable Long userId,
            @RequestBody NotificationPreferenceUpdateDTO updatedPreferences) {
        NotificationPreference preferences = preferenceService.updateUserPreferences(userId, updatedPreferences);
        return ResponseEntity.ok(preferences);
    }
}
