package com.example.statistics_service.service;

import com.example.statistics_service.dto.listener.LoggedUserEventDTO;

public interface IUserStatsService {

    void handleUserCreatedEvent();

    void handleUserLoggedEvent(LoggedUserEventDTO loggedUserEventDTO);

    Long getTotalUsersCreated();

    Long getActiveUsers();
}
