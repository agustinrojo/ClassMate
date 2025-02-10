package com.example.statistics_service.consumer;

import com.example.statistics_service.dto.listener.CommentCreatedStatisticDTO;
import com.example.statistics_service.dto.listener.PostCreatedStatisticDTO;
import com.example.statistics_service.entity.ForumActivity;
import com.example.statistics_service.repository.IForumActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCreatedStatisticListener {

    private final IForumActivityRepository repository;

    @RabbitListener(queues = "${rabbitmq.queue.post.created.statistic}")
    public void handlePostCreatedEvent(PostCreatedStatisticDTO event) {
        handleStatisticEvent(event.getForumId(), event.getTimestamp(), "POST");
    }

    @RabbitListener(queues = "${rabbitmq.queue.comment.created.statistic}")
    public void handleCommentCreatedEvent(CommentCreatedStatisticDTO event) {
        handleStatisticEvent(event.getForumId(), event.getTimestamp(), "COMMENT");
    }

    private void handleStatisticEvent(Long forumId, LocalDateTime timestamp, String eventType) {
        log.info("Received {} created event for forum ID {}: {}", eventType, forumId, timestamp);

        // Save event data to the database
        ForumActivity activity = new ForumActivity();
        activity.setEventType(eventType);
        activity.setTimestamp(timestamp);
        activity.setForumId(forumId);
        activity.setActivityCount(1);

        repository.save(activity);

        log.info("{} creation event saved to database: {}", eventType, activity);
    }
}