package com.example.Security.service;

import com.example.Security.dto.user.profile.ProfilePhotoDTO;
import com.example.Security.dto.user.profile.UserProfileResponseDTO;
import com.example.Security.dto.user.profile.UserProfileWithRoleDTO;
import com.example.Security.entities.Attachment;
import com.example.Security.entities.User;
import com.example.Security.entities.UserProfile;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserProfileResponseDTO> searchUserByNickname(String nickname, int page, int size){
        Page<User> users = userRepository.findByUserProfileNicknameContaining(nickname, PageRequest.of(page, size));
        List<User> usersList = users.getContent();
        if(usersList.isEmpty()){
            return new ArrayList<>();
        }
        return usersList.stream()
                .map(this::mapUserToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileResponseDTO> findMultipleUsers(List<Long> userIds){
        return userIds.stream()
                .map((Long userId) -> {
                    User existingUser = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    UserProfile userProfile = existingUser.getUserProfile();

                    if (userProfile == null) {
                        throw new IllegalArgumentException("User profile not found");
                    }
                    return getUserProfileResponseDTO(existingUser, userProfile);
                })
                .collect(Collectors.toList());
    }

    public UserProfileResponseDTO findUserProfileSearchById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        UserProfile userProfile = user.getUserProfile();

        if (userProfile == null) {
            throw new IllegalArgumentException("User profile not found");
        }
        return getUserProfileResponseDTO(user, userProfile);
    }

    public List<UserProfileWithRoleDTO> getUsersFromForum(Long forumId) {
        List<User> creators = userRepository.findByForumsCreatedContaining(forumId);
        List<User> admins = userRepository.findByForumsAdminContaining(forumId);
        List<User> subscribers = userRepository.findByForumsSubscribedContaining(forumId);

        List<UserProfileWithRoleDTO> userProfiles = new ArrayList<>();

        // Add creator first
        for(User creator : creators) {
            UserProfileWithRoleDTO userProfile = mapToUserProfileWithRoleDTO(creator, "Creator");
            userProfiles.add(userProfile);
        }

        // If not in list already, add admins
        for(User admin : admins) {
            if(!isUserInList(userProfiles, admin.getId())) {
                UserProfileWithRoleDTO userProfile = mapToUserProfileWithRoleDTO(admin, "Admin");
                userProfiles.add(userProfile);
            }
        }

        // If not in list already, add subscribers
        for(User subscriber : subscribers) {
            if(!isUserInList(userProfiles, subscriber.getId())) {
                UserProfileWithRoleDTO userProfile = mapToUserProfileWithRoleDTO(subscriber, "Subscriber");
                userProfiles.add(userProfile);
            }
        }

        return userProfiles;
    }

    public List<UserProfileWithRoleDTO> searchUsersInForum(Long forumId, String nicknameQuery){
        List<UserProfileWithRoleDTO> members = getUsersFromForum(forumId);

        return members.stream()
                .filter((UserProfileWithRoleDTO member) -> member.getNickname().contains(nicknameQuery))
                .collect(Collectors.toList());
    }

    private UserProfileWithRoleDTO mapToUserProfileWithRoleDTO(User user, String userType) {
        UserProfile userProfile = user.getUserProfile();
        return UserProfileWithRoleDTO.builder()
                .userId(user.getId())
                .nickname(userProfile.getNickname())
                .profilePhoto(convertToFileDTO(userProfile.getProfilePhoto()))
                .userType(userType)
                .build();
    }

    private boolean isUserInList(List<UserProfileWithRoleDTO> users, Long userId) {
        return users.stream().anyMatch(user -> user.getUserId().equals(userId));
    }

    public List<Long> getForumsAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));

        return user.getForumsAdmin();
    }


    private UserProfileResponseDTO mapUserToResponseDTO(User user) {
        UserProfile userProfile = user.getUserProfile();
        return UserProfileResponseDTO.builder()
                .userId(user.getId())
                .nickname(userProfile.getNickname())
                .profilePhoto(convertToFileDTO(userProfile.getProfilePhoto()))
                .description(userProfile.getDescription())
                .build();
    }

    private UserProfileResponseDTO getUserProfileResponseDTO(User user, UserProfile userProfile) {
        return UserProfileResponseDTO.builder()
                .userId(user.getId())
                .nickname(userProfile.getNickname())
                .profilePhoto(convertToFileDTO(userProfile.getProfilePhoto()))
                .description(userProfile.getDescription())
                .build();
    }

    private ProfilePhotoDTO convertToFileDTO(Attachment attachment) {
        return ProfilePhotoDTO.builder()
                .photoId(attachment.getId())
                .originalFilename(attachment.getOriginalFilename())
                .contentType(attachment.getContentType())
                .size(attachment.getSize())
                .build();
    }

}
