package com.example.Security.service;

import com.example.Security.dto.user.UserDisplayDTO;
import com.example.Security.dto.user.profile.*;
import com.example.Security.entities.Attachment;
import com.example.Security.entities.User;
import com.example.Security.entities.UserProfile;
import com.example.Security.exception.ResourceWithNumericValueDoesNotExistException;
import com.example.Security.publisher.CreateUserPublisher;
import com.example.Security.repositories.AttachmentRepository;
import com.example.Security.repositories.UserProfileRepository;
import com.example.Security.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private final CreateUserPublisher createUserPublisher;
    private static final long MAX_PROFILE_PHOTO_SIZE = 10 * 1048576; // 1MB

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, CreateUserPublisher createUserPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.createUserPublisher = createUserPublisher;
    }

    @Transactional
    public UserProfileResponseDTO createUserProfile(UserProfileRequestDTO requestDTO) throws IOException {
        LOGGER.info(String.format("Creating Profile for user with id '%d'", requestDTO.getUserId()));
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", requestDTO.getUserId()));
        System.out.println("llego aca");
        if (requestDTO.getProfilePhoto().getSize() > MAX_PROFILE_PHOTO_SIZE) {
            LOGGER.info("size not allowed");
            throw new IllegalArgumentException("Profile photo exceeds maximum allowed size");
        }

        Attachment profilePhoto = getProfilePhoto(requestDTO);

        UserProfile userProfile = getUserProfile(requestDTO, profilePhoto);

        user.setUserProfile(userProfile);
        userProfileRepository.save(userProfile);
        userRepository.save(user);

        createUserPublisher.publishCreateUserEvent(
                UserDisplayDTO.builder()
                .userId(requestDTO.getUserId())
                .nickname(requestDTO.getNickname())
                .build()
        );

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
        return attachmentRepository.findById(photoId)
                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("Attachment", "id", photoId));
    }

    private ProfilePhotoDTO convertToFileDTO(Attachment attachment) {
        return ProfilePhotoDTO.builder()
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



    public void updateUserProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO) throws IOException {
        User existingUser = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceWithNumericValueDoesNotExistException("User", "id", userId));
        UserProfile userProfile = existingUser.getUserProfile();
        userProfile.setNickname(userProfileUpdateDTO.getNickname());
        userProfile.setDescription(userProfileUpdateDTO.getDescription());

        System.out.println(userProfileUpdateDTO);

        if(!(userProfileUpdateDTO.getProfilePhotoUpdateDTO().getPhotoIdToRemove() == null)){
            System.out.println("entro a photo to id to remove");
            Long photoIdToRemove = userProfileUpdateDTO.getProfilePhotoUpdateDTO().getPhotoIdToRemove();
            userProfileRepository.deleteById(photoIdToRemove);
        }

        if(!(userProfileUpdateDTO.getProfilePhotoUpdateDTO().getPhotoToAdd() == null)){
            MultipartFile newPhoto = userProfileUpdateDTO.getProfilePhotoUpdateDTO().getPhotoToAdd();
            Attachment newAttachment = Attachment.builder()
                    .originalFilename(newPhoto.getOriginalFilename())
                    .size(newPhoto.getSize())
                    .contentType(newPhoto.getContentType())
                    .bytes(newPhoto.getBytes())
                    .build();
            System.out.println("entro a photo to add");
            System.out.println(newAttachment);
            userProfile.setProfilePhoto(newAttachment);
        }
        existingUser.setUserProfile(userProfile);
        userRepository.save(existingUser);
    }

}
