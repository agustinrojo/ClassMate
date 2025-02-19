package com.example.Security.publisher;


import com.example.Security.dto.statistics.LoggedUserEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticsPublisher {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.statistics}")
    private String statisticsExchange;
    @Value("${rabbitmq.user.created.statistic.routing-key}")
    private String userCreatedStatisticRoutingKey;
    @Value("${rabbitmq.user.logged.statistic.routing-key}")
    private String userLoggedStatisticRoutingKey;

    public UserStatisticsPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserCreatedEvent() {
        rabbitTemplate.convertAndSend(statisticsExchange, userCreatedStatisticRoutingKey, "User Created Statistics Event");
    }

    public void publishUserLoggedEvent(LoggedUserEventDTO event) {
        rabbitTemplate.convertAndSend(statisticsExchange, userLoggedStatisticRoutingKey, event);
    }
}
