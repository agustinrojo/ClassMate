package com.classmate.post_service.service.impl;

import com.classmate.post_service.dto.notification.MilestoneReachedEventDTO;
import com.classmate.post_service.dto.user.userReputation.ReputationAction;
import com.classmate.post_service.dto.user.userReputation.UserReputationChangeDTO;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.service.IPostValorationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostValorationServiceImpl implements IPostValorationService {

    private final IPostRepository postRepository;
    private final PostPublisher postPublisher;
    private final Logger LOGGER = LoggerFactory.getLogger(PostValorationServiceImpl.class);
    private static final List<Integer> MILESTONES = List.of(2, 10, 25, 50, 75, 100, 150, 200, 300, 400, 500, 1000);


    public PostValorationServiceImpl(IPostRepository postRepository, PostPublisher postPublisher) {
        this.postRepository = postRepository;
        this.postPublisher = postPublisher;
    }

    @Override
    @Transactional
    public void upvotePost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        post.addUpvote(userId);
        postRepository.save(post);

        checkForMilestone(post);

        updateUserReputation(post.getAuthor().getUserId(), ReputationAction.LIKE);
    }



    @Override
    @Transactional
    public void downvotePost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        post.addDownvote(userId);
        postRepository.save(post);

        updateUserReputation(post.getAuthor().getUserId(), ReputationAction.DISLIKE);
    }

    private void updateUserReputation(Long userIdToUpdate, ReputationAction action) {
        UserReputationChangeDTO event = UserReputationChangeDTO.builder()
                .userId(userIdToUpdate)
                .action(action)
                .build();

        postPublisher.publishUserReputationChange(event);
    }

    @Override
    @Transactional
    public void removeVoteFromPost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        ReputationAction action = post.removeVote(userId);
        postRepository.save(post);

        updateUserReputation(post.getAuthor().getUserId(), action);
    }

    private void checkForMilestone(Post post) {
        int currentValoration = post.getValoration();

        // Find the highest milestone reached that is larger than lastMilestone but smaller than currentValoration
        Optional<Integer> milestone = MILESTONES.stream()
                .filter(m -> m > post.getLastMilestone() && currentValoration >= m)
                .max(Integer::compareTo);

        if (milestone.isPresent()) {
            post.setLastMilestone(milestone.get());  // Update the last milestone

            // Save the post to persist the milestone change
            postRepository.save(post);

            // Publish the event to the notifications service
            this.publishMilestoneReachedEvent(post, milestone.get());
        }

    }
    private void publishMilestoneReachedEvent(Post post, int milestone) {
        // Create the event DTO
        MilestoneReachedEventDTO event = MilestoneReachedEventDTO.builder()
                .postId(post.getId())
                .authorId(post.getAuthor().getUserId())
                .forumId(post.getForumId())
                .milestone(milestone)
                .title(post.getTitle())
                .milestoneType("COMMENT")
                .build();

        LOGGER.info(String.format("Publishing milestone reached event with id -> {}", event.getPostId()));

        // Publish the event to the notifications service
        postPublisher.publishMilestoneReachedEvent(event);
    }
}
