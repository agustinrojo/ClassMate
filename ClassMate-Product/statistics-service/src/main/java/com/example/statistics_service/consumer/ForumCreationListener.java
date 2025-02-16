package com.example.statistics_service.consumer;


import com.example.statistics_service.service.IForumCreationStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForumCreationListener {

    private final IForumCreationStatsService forumCreationStatsService;

    public ForumCreationListener(IForumCreationStatsService forumCreationStatsService) {
        this.forumCreationStatsService = forumCreationStatsService;
    }

    // Listening to the RabbitMQ event for forum creation
    @RabbitListener(queues = "${rabbitmq.queue.forum.created.statistic}")
    public void onForumCreatedEvent(String message) {
        log.info("Received forum creation event");
        // You can add message parsing if necessary, this is just to trigger the stat update
        forumCreationStatsService.handleForumCreatedEvent();
    }
}