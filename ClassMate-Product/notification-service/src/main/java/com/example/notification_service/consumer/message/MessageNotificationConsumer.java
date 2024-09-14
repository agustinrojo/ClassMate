package com.example.notification_service.consumer.message;


import com.example.notification_service.consumer.comment.CommentNotificationConsumer;
import com.example.notification_service.dto.publisherAndConsumerDTOS.message.MessageNotificationEventDTO;
import com.example.notification_service.dto.publisherAndConsumerDTOS.message.MessageSenderNameRequestDTO;
import com.example.notification_service.publisher.NotificationPublisher;
import com.example.notification_service.repository.MessageNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageNotificationConsumer {
    private final NotificationPublisher notificationPublisher;
    private final MessageNotificationRepository messageNotificationRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(CommentNotificationConsumer.class);

    public MessageNotificationConsumer(NotificationPublisher notificationPublisher, MessageNotificationRepository messageNotificationRepository) {
        this.notificationPublisher = notificationPublisher;
        this.messageNotificationRepository = messageNotificationRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notifications.message-queue}")
    public void handleMessageNotification(MessageNotificationEventDTO event) {
        // Check if an unread message notification from the same sender already exists
        boolean unreadNotificationExists = messageNotificationRepository.existsByUserIdAndSenderIdAndIsSeenFalse(
                event.getReceiverId(),
                event.getSenderId());

        if(!unreadNotificationExists) {
            MessageSenderNameRequestDTO messageSenderNameRequestDTO = new MessageSenderNameRequestDTO(event.getReceiverId(), event.getSenderId());
            notificationPublisher.publishMessageSenderNameRequestEvent(messageSenderNameRequestDTO);
            LOGGER.info("Requesting message sender name with user ID: " + messageSenderNameRequestDTO.getSenderId());
        } else {
            LOGGER.info("Unread message notification already exists for user ID: {}, skipping notification for sender ID: {}",
                    event.getReceiverId(), event.getSenderId());
        }
    }
}
