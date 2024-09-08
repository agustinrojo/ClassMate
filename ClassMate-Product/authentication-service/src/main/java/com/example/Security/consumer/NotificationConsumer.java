package com.example.Security.consumer;

import com.example.Security.dto.message.MessageSenderNameRequestDTO;
import com.example.Security.dto.message.MessageSenderNameResponseDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.publisher.NotificationPublisher;
import com.example.Security.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class NotificationConsumer {
    Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);
    private final UserRepository userRepository;
    private final NotificationPublisher notificationPublisher;

    public NotificationConsumer(UserRepository userRepository, NotificationPublisher notificationPublisher) {
        this.userRepository = userRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.message-sender-request-queue}")
    public void handleMessageSenderNameRequest(MessageSenderNameRequestDTO event) {
        User user = userRepository.findByIdWithUserProfile(event.getSenderId())
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", event.getSenderId()));

        if (user != null) {
            MessageSenderNameResponseDTO responseEvent = new MessageSenderNameResponseDTO(
                event.getReceiverId(),
                    event.getSenderId(),
                    user.getUserProfile().getNickname()
            );
            notificationPublisher.publishMessageSenderNameResponseEvent(responseEvent);
        } else {
            LOGGER.warn("User not found for handleMessageSenderNameRequest()");
        }
    }
}
