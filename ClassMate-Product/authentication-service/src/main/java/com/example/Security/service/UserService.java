package com.example.Security.service;

import com.example.Security.dto.ForumSubscriptionDTO;
import com.example.Security.entities.User;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;

    public void subscribeToForum(ForumSubscriptionDTO forumSubscriptionDTO){
        Long userId = forumSubscriptionDTO.getUserId();
        Long forumId = forumSubscriptionDTO.getForumId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        if(user.isAlreadySubscribedToForum(forumId)){
            LOGGER.error(String.format("User %d already subscribed to forum %d.", userId, forumId));
            return;
        }

        user.subscribeToForum(forumId);
        LOGGER.info(String.format("User %d subscribed to forum %d.", userId, forumId));

        userRepository.save(user);

    }

}
