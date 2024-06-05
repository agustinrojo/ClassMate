package com.classmate.forum_service.service.impl;

import com.classmate.forum_service.client.IPostClient;
import com.classmate.forum_service.dto.APIResponseDTO;
import com.classmate.forum_service.dto.ForumResponseDTO;
import com.classmate.forum_service.dto.ForumSubscriptionDTO;
import com.classmate.forum_service.dto.PostDTO;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.entity.Forum;
import com.classmate.forum_service.exception.ForumNotFoundException;
import com.classmate.forum_service.exception.InvalidForumException;
import com.classmate.forum_service.exception.UnauthorizedActionException;
import com.classmate.forum_service.mapper.IForumMapper;
import com.classmate.forum_service.repository.IForumRepository;
import com.classmate.forum_service.service.IForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ForumService interface for managing forums.
 */
@Service
public class ForumServiceImpl implements IForumService {

    @Value("${rabbitmq.forum-exchange.name}")
    private String forumExchange;
    @Value("${rabbitmq.exchange.routing-key}")
    private String subscriptionRoutingKey;
    private final IForumRepository forumRepository;
    private final IForumMapper forumMapper;
    private final IPostClient postClient;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumServiceImpl.class);

    /**
     * Constructs a new ForumServiceImpl with the specified repository, mapper, and post client.
     *
     * @param forumRepository the forum repository
     * @param forumMapper the forum mapper
     * @param postClient the post client
     */
    public ForumServiceImpl(IForumRepository forumRepository, IForumMapper forumMapper, IPostClient postClient, RabbitTemplate rabbitTemplate) {
        this.forumRepository = forumRepository;
        this.forumMapper = forumMapper;
        this.postClient = postClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForumResponseDTO> getAllForums() {
        LOGGER.info("Getting all forums...");
        return forumRepository.findAll().stream()
                .map(forumMapper::convertToForumResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APIResponseDTO getForumById(Long id) {
        LOGGER.info("Getting forum by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));

        List<PostDTO> postDTOS = postClient.getPostsByForumId(id, 0, 10);
        APIResponseDTO responseDTO = forumMapper.convertToAPIResponseDTO(forum);
        responseDTO.setPosts(postDTOS);

        return responseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForumResponseDTO> getForumsByTitle(String title, int page, int size) {
        LOGGER.info("Getting forums by title...");
        Page<Forum> postsPage = forumRepository.findByTitleContainingIgnoreCaseOrderByCreationDateDesc(title, PageRequest.of(page, size));
        return postsPage.getContent().stream().map(forumMapper::convertToForumResponseDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ForumResponseDTO saveForum(ForumRequestDTO forumRequestDTO, Long creatorId) {
        validateForum(forumRequestDTO);
        LOGGER.info("Saving forum...");
        Forum forum = forumMapper.convertToForum(forumRequestDTO);
        forum.setCreatorId(creatorId);
        forum.setMemberIds(new ArrayList<>());
        forum.setAdminIds(new ArrayList<>());
        forum.addMember(creatorId);
        forum.addAdmin(creatorId);
        Forum savedForum = forumRepository.save(forum);
        return forumMapper.convertToForumResponseDTO(savedForum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateForum(Long id, ForumRequestDTO forumRequestDTO) {
        validateForum(forumRequestDTO);
        LOGGER.info("Updating forum by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));
        forum.setTitle(forumRequestDTO.getTitle());
        forum.setDescription(forumRequestDTO.getDescription());
        forumRepository.save(forum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteForum(Long id, Long userId) {
        LOGGER.info("Deleting forum by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));
        if (!forum.getCreatorId().equals(userId)) {
            throw new UnauthorizedActionException("User not authorized to delete this forum");
        }

        forumRepository.delete(forum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMember(Long forumId, Long memberId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        forum.addMember(memberId);

        ForumSubscriptionDTO forumSubscriptionDTO = ForumSubscriptionDTO.builder()
                                                    .forumId(forumId)
                                                    .userId(memberId)
                                                    .build();

        rabbitTemplate.convertAndSend(forumExchange, subscriptionRoutingKey, forumSubscriptionDTO);
        forumRepository.save(forum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAdmin(Long forumId, Long adminId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        forum.addAdmin(adminId);
        forumRepository.save(forum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMember(Long forumId, Long memberId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        forum.removeMember(memberId);
        forumRepository.save(forum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAdmin(Long forumId, Long adminId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        forum.removeAdmin(adminId);
        forumRepository.save(forum);
    }

    /**
     * Validates the forum data.
     *
     * @param forumRequestDTO the forum data to validate
     */
    private void validateForum(ForumRequestDTO forumRequestDTO) {
        if (forumRequestDTO.getTitle() == null || forumRequestDTO.getTitle().isEmpty()) {
            throw new InvalidForumException("Forum title cannot be empty");
        }
        if (forumRequestDTO.getDescription() == null || forumRequestDTO.getDescription().isEmpty()) {
            throw new InvalidForumException("Forum description cannot be empty");
        }
        if (forumRequestDTO.getTitle().length() > 100) {
            throw new InvalidForumException("Forum title cannot exceed 100 characters");
        }
        if (forumRequestDTO.getDescription().length() > 300) {
            throw new InvalidForumException("Forum description cannot exceed 300 characters");
        }
    }
}
