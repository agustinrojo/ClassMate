package com.example.calendar_service.publisher;

import com.example.calendar_service.dto.SyncDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SyncPublisher {

    private final RabbitTemplate rabbitTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(SyncPublisher.class);

    @Value("${rabbitmq.queue.sync-google-queue}")
    private String syncGoogleQueue;

    @Value("${rabbitmq.queue.unsync-google-queue}")
    private String unsyncGoogleQueue;

    @Value("${rabbitmq.sync-exchange.name}")
    private String syncExchange;

    @Value("${rabbitmq.exchange.sync-google-routing-key}")
    private String syncRoutingKey;

    public SyncPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSyncGoogle(Long userId, boolean isSynced){
        SyncDTO syncDTO = SyncDTO.builder()
                        .userId(userId)
                        .isSynced(isSynced)
                        .build();
        LOGGER.info(String.format("SyncDTO sent to RabbitMQ -> %s", syncDTO));
        rabbitTemplate.convertAndSend(syncExchange, syncRoutingKey, syncDTO);
    }
}
