package com.example.Security.service;

import com.example.Security.dto.user.profile.UserProfileSearchDTO;
import com.example.Security.entities.Attachment;
import com.example.Security.entities.User;
import com.example.Security.entities.UserProfile;
import com.example.Security.repositories.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserProfileSearchDTO> searchUserByNickname(String nickname, int page, int size){
        Page<User> users = userRepository.findByUserProfileNicknameContaining(nickname, PageRequest.of(page, size));
        List<User> usersList = users.getContent();
        if(usersList.isEmpty()){
            return new ArrayList<>();
        }
        return usersList.stream()
                .map(this::mapUserToSearchDTO)
                .collect(Collectors.toList());
    }

    private UserProfileSearchDTO mapUserToSearchDTO(User user){
        return UserProfileSearchDTO.builder()
                .userId(user.getId())
                .nickname(user.getUserProfile().getNickname())
                .profilePhoto(new ByteArrayResource(user.getUserProfile().getProfilePhoto().getBytes()))
                .build();
    }


}
