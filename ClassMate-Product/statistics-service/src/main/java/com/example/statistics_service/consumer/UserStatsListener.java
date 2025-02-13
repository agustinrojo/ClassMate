package com.example.statistics_service.consumer;


import com.example.statistics_service.dto.listener.LoggedUserEventDTO;
import com.example.statistics_service.service.IForumCreationStatsService;
import com.example.statistics_service.service.IUserStatsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserStatsListener {
    private final IUserStatsService userStatsService;

    public UserStatsListener(IUserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    // Listening to the RabbitMQ event for forum creation
    @RabbitListener(queues = "${rabbitmq.queue.user.created.statistic}")
    public void onUserCreatedEvent(String message) {
        userStatsService.handleUserCreatedEvent();
    }

    // Listening to the RabbitMQ event for user logged
    @RabbitListener(queues = "${rabbitmq.queue.user.logged.statistic}")
    public void onUserLoggedEvent(LoggedUserEventDTO event) {
        userStatsService.handleUserLoggedEvent(event);
    }
}
