package com.example.statistics_service.service.impl;

import com.example.statistics_service.dto.listener.LoggedUserEventDTO;
import com.example.statistics_service.entity.LoggedUser;
import com.example.statistics_service.entity.UsersCreated;
import com.example.statistics_service.repository.IUserCreatedStatsRepository;
import com.example.statistics_service.repository.IUserLoggedStatsRepository;
import com.example.statistics_service.service.IUserStatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserStatsService implements IUserStatsService {
    private final IUserCreatedStatsRepository userCreatedRepository;
    private final IUserLoggedStatsRepository userLoggedRepository;

    public UserStatsService(IUserCreatedStatsRepository userCreatedRepository, IUserLoggedStatsRepository userLoggedRepository) {
        this.userCreatedRepository = userCreatedRepository;
        this.userLoggedRepository = userLoggedRepository;
    }

    @Override
    public void handleUserCreatedEvent() {
        UsersCreated usersCreated = userCreatedRepository.findById(1L).orElseGet(() -> {
            // If no record is found, create a new one
            UsersCreated newUserStats = new UsersCreated();
            newUserStats.setUsersCreatedCount(0L);  // Start count at 0
            return userCreatedRepository.save(newUserStats);  // Save and return the new entity
        });

        // Increment the usersCreatedCount by 1
        usersCreated.setUsersCreatedCount(usersCreated.getUsersCreatedCount() + 1);

        // Save the updated entity back to the repository
        userCreatedRepository.save(usersCreated);
    }

    @Override
    public void handleUserLoggedEvent(LoggedUserEventDTO loggedUserEventDTO) {
        LoggedUser loggedUser = new LoggedUser();
        loggedUser.setUserId(loggedUserEventDTO.getUserID());
        loggedUser.setLoggedDateTime(loggedUserEventDTO.getLoggedDateTime());

        // Save the logged user data
        userLoggedRepository.save(loggedUser);
    }

    @Override
    public Long getTotalUsersCreated() {
        UsersCreated usersCreated = userCreatedRepository.findById(1L).orElse(null);
        return (usersCreated != null) ? usersCreated.getUsersCreatedCount() : 0L;
    }

    @Override
    public Long getActiveUsers() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return userLoggedRepository.countByLoggedDateTimeAfter(oneWeekAgo);
    }
}
