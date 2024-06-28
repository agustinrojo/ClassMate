package com.example.Security.service;

import com.example.Security.dto.user.FileDTO;
import com.example.Security.dto.user.UserProfileRequestDTO;
import com.example.Security.dto.user.UserProfileResponseDTO;
import com.example.Security.entities.Attachment;
import com.example.Security.entities.User;
import com.example.Security.entities.UserProfile;
import com.example.Security.repositories.UserProfileRepository;
import com.example.Security.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private static final long MAX_PROFILE_PHOTO_SIZE = 1048576; // 1MB

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfileResponseDTO createUserProfile(UserProfileRequestDTO requestDTO) throws IOException {
        LOGGER.info(String.format("Creating Profile for user with id '%d'", requestDTO.getUserId()));
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (requestDTO.getProfilePhoto().getSize() > MAX_PROFILE_PHOTO_SIZE) {
            throw new IllegalArgumentException("Profile photo exceeds maximum allowed size");
        }

        Attachment profilePhoto = getProfilePhoto(requestDTO);

        UserProfile userProfile = getUserProfile(requestDTO, profilePhoto);

        user.setUserProfile(userProfile);
        userProfileRepository.save(userProfile);
        userRepository.save(user);

        return getUserProfileResponseDTO(user, userProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDTO getUserProfile(Long userId) {
        LOGGER.info(String.format("Getting Profile for user with id '%d'", userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile not found");
        }

        return getUserProfileResponseDTO(user, userProfile);
    }

    @Transactional(readOnly = true)
    public Attachment getProfilePhotoById(Long photoId) {
        LOGGER.info(String.format("Getting Profile Photo from Profile with id '%d'", photoId));
        return userProfileRepository.findById(photoId)
                .map(UserProfile::getProfilePhoto)
                .orElseThrow(() -> new IllegalArgumentException("Profile photo not found"));
    }

    private FileDTO convertToFileDTO(Attachment attachment) {
        return FileDTO.builder()
                .photoId(attachment.getId())
                .originalFilename(attachment.getOriginalFilename())
                .contentType(attachment.getContentType())
                .size(attachment.getSize())
                .build();
    }

    private Attachment getProfilePhoto(UserProfileRequestDTO requestDTO) throws IOException {
        return Attachment.builder()
                .bytes(requestDTO.getProfilePhoto().getBytes())
                .originalFilename(requestDTO.getProfilePhoto().getOriginalFilename())
                .contentType(requestDTO.getProfilePhoto().getContentType())
                .size(requestDTO.getProfilePhoto().getSize())
                .build();
    }

    private UserProfile getUserProfile(UserProfileRequestDTO requestDTO, Attachment profilePhoto) {
        return UserProfile.builder()
                .nickname(requestDTO.getNickname())
                .profilePhoto(profilePhoto)
                .description(requestDTO.getDescription())
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
}
