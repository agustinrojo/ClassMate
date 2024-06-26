package com.classmate.post_service.service.impl;

import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.PostNotFoundException;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.service.IPostValorationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostValorationServiceImpl implements IPostValorationService {

    private final IPostRepository postRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(PostValorationServiceImpl.class);

    public PostValorationServiceImpl(IPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void upvotePost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        post.addUpvote(userId);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void downvotePost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        post.addDownvote(userId);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void removeVoteFromPost(Long postId, Long userId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id: '%d' not found", postId)));

        post.removeVote(userId);
        postRepository.save(post);
    }
}
