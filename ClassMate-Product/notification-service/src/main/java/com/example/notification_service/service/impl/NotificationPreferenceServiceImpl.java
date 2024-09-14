package com.example.notification_service.service.impl;

import com.example.notification_service.dto.preference.NotificationPreferenceUpdateDTO;
import com.example.notification_service.entity.notification.NotificationPreference;
import com.example.notification_service.repository.NotificationPreferenceRepository;
import com.example.notification_service.service.NotificationPreferenceService;
import org.springframework.stereotype.Service;

@Service
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {
    private final NotificationPreferenceRepository preferenceRepository;


    public NotificationPreferenceServiceImpl(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }


    @Override
    public NotificationPreference getUserPreferences(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferencesForUser(userId)); // Create or return default preferences
    }

    private NotificationPreference createDefaultPreferencesForUser(Long userId) {
        NotificationPreference defaultPreferences = new NotificationPreference();
        defaultPreferences.setUserId(userId);
        defaultPreferences.setCommentNotificationEnabled(true);
        defaultPreferences.setEventNotificationEnabled(true);
        defaultPreferences.setLikeNotificationEnabled(true);
        defaultPreferences.setMessageNotificationEnabled(true);
        return preferenceRepository.save(defaultPreferences);
    }

    @Override
    public NotificationPreference updateUserPreferences(Long userId, NotificationPreferenceUpdateDTO updatedPreferences) {
        NotificationPreference preferences = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found")); // TODO: Exception handling

        preferences.setCommentNotificationEnabled(updatedPreferences.getCommentNotificationEnabled());
        preferences.setLikeNotificationEnabled(updatedPreferences.getLikeNotificationEnabled());
        preferences.setMessageNotificationEnabled(updatedPreferences.getMessageNotificationEnabled());
        preferences.setEventNotificationEnabled(updatedPreferences.getEventNotificationEnabled());

        return preferenceRepository.save(preferences);
    }
}
