package com.example.statistics_service.consumer;


import com.example.statistics_service.service.IForumCreationStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ForumCreationListener {

    private final IForumCreationStatsService forumCreationStatsService;

    public ForumCreationListener(IForumCreationStatsService forumCreationStatsService) {
        this.forumCreationStatsService = forumCreationStatsService;
    }

    // Listening to the RabbitMQ event for forum creation
    @RabbitListener(queues = "${rabbitmq.queue.forum.created.statistic}")
    public void onForumCreatedEvent(String message) {
        // You can add message parsing if necessary, this is just to trigger the stat update
        forumCreationStatsService.handleForumCreatedEvent();
    }
}