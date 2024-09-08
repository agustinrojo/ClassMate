package com.example.notification_service.service;

import com.example.notification_service.dto.preference.NotificationPreferenceUpdateDTO;
import com.example.notification_service.entity.notification.NotificationPreference;

import java.util.Optional;

public interface NotificationPreferenceService {

    NotificationPreference getUserPreferences(Long userId);

    NotificationPreference updateUserPreferences(Long userId, NotificationPreferenceUpdateDTO updatedPreferences);
}
