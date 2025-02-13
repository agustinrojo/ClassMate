package com.classmate.forum_service.service.impl;

import com.classmate.forum_service.client.IAuthClient;
import com.classmate.forum_service.client.IPostClient;
import com.classmate.forum_service.dto.*;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.forum_service.dto.delete_request.DeleteRequestDTOResponse;
import com.classmate.forum_service.dto.delete_request.ForumDeleteRequestDTOResponse;
import com.classmate.forum_service.dto.user.BanUserDeleteMemberEventDTO;
import com.classmate.forum_service.dto.user.GetUserProfileResponseDTO;
import com.classmate.forum_service.dto.user.UserDTO;
import com.classmate.forum_service.entity.DeleteRequest;
import com.classmate.forum_service.entity.Forum;
import com.classmate.forum_service.entity.enums.Role;
import com.classmate.forum_service.exception.ForumNotFoundException;
import com.classmate.forum_service.exception.InvalidForumException;
import com.classmate.forum_service.exception.UnauthorizedActionException;
import com.classmate.forum_service.exception.UserBannedException;
import com.classmate.forum_service.mapper.DeleteRequestMapper;
import com.classmate.forum_service.mapper.IForumMapper;
import com.classmate.forum_service.publisher.ForumSubscriptionPublisher;
import com.classmate.forum_service.repository.IForumRepository;
import com.classmate.forum_service.service.IForumService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ForumService interface for managing forums.
 */
@Service
public class ForumServiceImpl implements IForumService {

    private final IForumRepository forumRepository;
    private final IForumMapper forumMapper;
    private final IPostClient postClient;
    private final ForumSubscriptionPublisher subscriptionPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumServiceImpl.class);
    private final ForumSubscriptionPublisher forumSubscriptionPublisher;
    private final DeleteRequestMapper deleteRequestMapper;
    private final IAuthClient authClient;

    /**
     * Constructs a new ForumServiceImpl with the specified repository, mapper, post client, and subscription publisher.
     *
     * @param forumRepository the forum repository
     * @param forumMapper the forum mapper
     * @param postClient the post client
     * @param subscriptionPublisher the subscription publisher
     */
    public ForumServiceImpl(IForumRepository forumRepository, IForumMapper forumMapper, IPostClient postClient, ForumSubscriptionPublisher subscriptionPublisher, ForumSubscriptionPublisher forumSubscriptionPublisher, DeleteRequestMapper deleteRequestMapper, IAuthClient authClient) {
        this.forumRepository = forumRepository;
        this.forumMapper = forumMapper;
        this.postClient = postClient;
        this.subscriptionPublisher = subscriptionPublisher;
        this.forumSubscriptionPublisher = forumSubscriptionPublisher;
        this.deleteRequestMapper = deleteRequestMapper;
        this.authClient = authClient;
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
    public APIResponseDTO getForumById(Long id, Long userId) {
        LOGGER.info("Getting forum by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));

        // Check if the user is banned
        if (forum.getBannedUsersIds().contains(userId)) {
            throw new UserBannedException("User banned from forum");
        }

        List<PostResponseDTO> postDTOS = postClient.getPostsByForumId(id, userId,0, 10);

        APIResponseDTO responseDTO = forumMapper.convertToAPIResponseDTO(forum);
        responseDTO.setPosts(postDTOS);
        boolean isCreator = Objects.equals(forum.getCreatorId(), userId);
        boolean isAdmin = forum.getAdminIds().contains(userId);
        responseDTO.setCreator(isCreator);
        responseDTO.setAdmin(isAdmin);
        responseDTO.setReportedByUser(forum.getDeleteRequests()
                                                .stream()
                                                .anyMatch((DeleteRequest deleteRequest) -> deleteRequest.getReporterId().equals(userId)));

        return responseDTO;
    }

    @Override
    public List<ForumResponseDTO> getMultipleForumsByIds(List<Long> ids, int page, int size) {
        Page<Forum> forums = forumRepository.findByIdIn(ids, PageRequest.of(page, size));
        return forums.stream()
                .map(forumMapper::convertToForumResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ForumSidebarDataDTO getForumSidebarDataById(Long id) {
        LOGGER.info("Getting forum sidebar data by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));

        return new ForumSidebarDataDTO(id, forum.getTitle());
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

    public ForumExistsDTO forumExists(Long forumId){
        Optional<Forum> existingForum = forumRepository.findById(forumId);
        ForumExistsDTO forumExistsDTO = ForumExistsDTO.builder().exists(false).build();
        if(existingForum.isPresent()){
            forumExistsDTO.setExists(true);
        }
        return forumExistsDTO;
    }

    public IsForumCreatorDTO isForumCreator(Long forumId ,Long userId){
        Optional<Forum> existingForum = forumRepository.findById(forumId);
        if(existingForum.isPresent()){
            Forum forum = existingForum.get();
            return IsForumCreatorDTO.builder().isAuthor(forum.getCreatorId().equals(userId)).build();
        } else {
            return IsForumCreatorDTO.builder().isAuthor(false).build();
        }
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
        forum.setHasBeenEdited(false);
        Forum savedForum = forumRepository.save(forum);

        ForumSubscriptionDTO creatorDTO = ForumSubscriptionDTO.builder()
                .forumId(forum.getId())
                .userId(creatorId)
                .build();
        subscriptionPublisher.publishCreatorUpdate(creatorDTO);

        subscriptionPublisher.publishForumCreatedEvent();
        
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
        if(!forum.getHasBeenEdited()){
            forum.setTitle(forumRequestDTO.getTitle());
            forum.setDescription(forumRequestDTO.getDescription());
            forum.setHasBeenEdited(true);
            forumRepository.save(forum);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteForum(Long id, Long userId, Role role) {
        LOGGER.info("Deleting forum by id...");
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + id));
        if (!forum.getCreatorId().equals(userId) && !role.equals(Role.ADMIN)) {
            throw new UnauthorizedActionException("User not authorized to delete this forum");
        }

        forumRepository.delete(forum);

        ForumDeletionDTO forumDeletionDTO = new ForumDeletionDTO(id);
        subscriptionPublisher.publishForumDeletion(forumDeletionDTO);
        subscriptionPublisher.publishForumSubscriptionDeletion(forumDeletionDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMember(Long forumId, Long memberId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));

        if (forum.getBannedUsersIds().contains(memberId)) {
            throw new UserBannedException("User banned from forum");
        }

        try {
            forum.addMember(memberId);
            ForumSubscriptionDTO forumSubscriptionDTO = ForumSubscriptionDTO.builder()
                    .forumId(forumId)
                    .userId(memberId)
                    .build();
            subscriptionPublisher.publishSubscription(forumSubscriptionDTO);
            forumRepository.save(forum);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAdmin(Long forumId, Long adminId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        try {
            forum.addAdmin(adminId);
            ForumSubscriptionDTO adminDTO = ForumSubscriptionDTO.builder()
                    .forumId(forumId)
                    .userId(adminId)
                    .build();
            subscriptionPublisher.publishAddAdmin(adminDTO);
            forumRepository.save(forum);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMember(Long forumId, Long memberId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        try {
            forum.removeMember(memberId);
            ForumSubscriptionDTO memberDTO = ForumSubscriptionDTO.builder()
                    .forumId(forumId)
                    .userId(memberId)
                    .build();
            subscriptionPublisher.publishRemoveMember(memberDTO);
            forumRepository.save(forum);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAdmin(Long forumId, Long adminId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));
        try {
            forum.removeAdmin(adminId);
            ForumSubscriptionDTO adminDTO = ForumSubscriptionDTO.builder()
                    .forumId(forumId)
                    .userId(adminId)
                    .build();
            subscriptionPublisher.publishRemoveAdmin(adminDTO);
            forumRepository.save(forum);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void banUser(Long forumId, Long bannerId, Long bannedId) {
        LOGGER.info("Banning user");
        // Get the forum
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException("Forum not found with id: " + forumId));

        // Check if the banner is the creator
        boolean isBannerCreator = forum.getCreatorId().equals(bannerId);

        // Check if the banner is an admin/moderator
        boolean isBannerAdmin = forum.getAdminIds().contains(bannerId);

        // Check if the banner is a subscriber (not creator or admin)
        boolean isBannerSubscriber = !isBannerCreator && !isBannerAdmin;

        // Check if the banned user is a creator, admin, or subscriber
        boolean isBannedUserAdmin = forum.getAdminIds().contains(bannedId);
        boolean isBannedUserCreator = forum.getCreatorId().equals(bannedId);

        // Validate banner's permission
        if (isBannerSubscriber) {
            throw new UserBannedException("Subscribers cannot ban users.");
        } else if (isBannerAdmin) {
            if (isBannedUserAdmin || isBannedUserCreator) {
                throw new UserBannedException("Moderators cannot ban other moderators or the creator.");
            }
        }
        // If creator, they can ban anyone

        // Add the banned user to the list if they are not already banned
        if (!forum.getBannedUsersIds().contains(bannedId)) {
            LOGGER.info("Entered ban");

            // Remove the banned user from the admin list, if they are an admin
            if (isBannedUserAdmin) {
                forum.getAdminIds().remove(bannedId);
                LOGGER.info("User {} removed from admin list for forum {}", bannedId, forumId);
            } else {
                LOGGER.info("User {} was not an admin for forum {}", bannedId, forumId);
            }

            // Remove the banned user from the member list, if they are a member
            if (forum.getMemberIds().contains(bannedId)) {
                forum.getMemberIds().remove(bannedId);
                LOGGER.info("User {} removed from member list for forum {}", bannedId, forumId);
            } else {
                LOGGER.info("User {} was not a member of forum {}", bannedId, forumId);
            }

            // Add the user to the banned users list
            forum.getBannedUsersIds().add(bannedId);
            forumRepository.save(forum); // Save changes

            // Publish event to delete user membership from Authentication Service
            forumSubscriptionPublisher.publishBanUserDeleteMemberEvent(new BanUserDeleteMemberEventDTO(bannedId, forumId));
        } else {
            LOGGER.info("User {} is already banned from forum {}", bannedId, forumId);
        }
    }

    @Override
    public void reportForum(Long forumId, DeleteRequestDTO deleteRequestDTO) {
        Forum reportedForum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException(String.format("Forum with id: %d not found.", forumId)));
        DeleteRequest deleteRequest = deleteRequestMapper.mapToEntity(deleteRequestDTO);
        reportedForum.addDeleteRequest(deleteRequest);

        forumRepository.save(reportedForum);
    }

    @Override
    public List<ForumDeleteRequestDTOResponse> getReportedForums(int page, int size, String authorizationHeader) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Forum> reportedForums = forumRepository.findAllWithDeleteRequests(pageable);
        return reportedForums
                .stream()
                .map((Forum forum) -> getForumDeleteRequestDTO(forum, authorizationHeader))
                .toList();
    }

    @Override
    public List<ForumDeleteRequestDTOResponse> findReportedForumsByKeyword(String keyword, String authorizationHeader) {
        List<Forum> reportedForums = forumRepository.findByDeleteRequestsAndKeyword(keyword);
        System.out.println(reportedForums);
        if(reportedForums.isEmpty()){
            return new ArrayList<>();
        }
        return reportedForums
                .stream()
                .map((Forum forum) -> getForumDeleteRequestDTO(forum, authorizationHeader))
                .toList();
    }

    @Override
    @Transactional
    public void absolveForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new ForumNotFoundException(String.format("Forum with id: %d not found", forumId)));
        forum.clearDeleteRequests();
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

    private ForumDeleteRequestDTOResponse getForumDeleteRequestDTO(Forum forum, String authorizationHeader){
        List<DeleteRequestDTOResponse> deleteRequests = forum.getDeleteRequests()
                .stream()
                .map((DeleteRequest deleteRequest) -> {
                    GetUserProfileResponseDTO user = authClient.getUserProfile(deleteRequest.getReporterId(), authorizationHeader);
                    return DeleteRequestDTOResponse
                            .builder()
                            .message(deleteRequest.getMessage())
                            .reporterId(deleteRequest.getReporterId())
                            .reporterNickname(user.getNickname())
                            .build();
                })
                .toList();
        return ForumDeleteRequestDTOResponse
                .builder()
                .id(forum.getId())
                .creationDate(forum.getCreationDate())
                .title(forum.getTitle())
                .description(forum.getDescription())
                .hasBeenEdited(forum.getHasBeenEdited())
                .deleteRequests(deleteRequests)
                .build();
    }
}
