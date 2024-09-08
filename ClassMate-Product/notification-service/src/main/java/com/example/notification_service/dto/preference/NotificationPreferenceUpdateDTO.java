package com.example.notification_service.dto.preference;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceUpdateDTO {
    private Boolean commentNotificationEnabled;
    private Boolean likeNotificationEnabled;
    private Boolean messageNotificationEnabled;
    private Boolean eventNotificationEnabled;
}
