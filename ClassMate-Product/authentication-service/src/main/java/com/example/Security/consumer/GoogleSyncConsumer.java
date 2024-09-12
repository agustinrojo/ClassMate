package com.example.Security.consumer;

import com.example.Security.dto.forum.ForumSubscriptionDTO;
import com.example.Security.dto.google_sync.SyncDTO;
import com.example.Security.entities.User;
import com.example.Security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleSyncConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSyncConsumer.class);

    private final UserRepository userRepository;

    public GoogleSyncConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.sync-google-queue}")
    public void subscribeToForum(SyncDTO syncDTO) {
        handleSync(syncDTO);
    }


    private void handleSync(SyncDTO syncDTO){
        User user = userRepository.findById(syncDTO.getUserId())
                .orElseThrow();

        user.setGoogleSynced(syncDTO.isSynced());
        userRepository.save(user);
    }
}
