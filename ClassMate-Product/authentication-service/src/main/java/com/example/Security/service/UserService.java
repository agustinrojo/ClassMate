package com.example.Security.service;

import com.example.Security.dto.user.profile.UserProfileSearchDTO;
import com.example.Security.entities.Attachment;
import com.example.Security.entities.User;
import com.example.Security.entities.UserProfile;
import com.example.Security.repositories.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileSearchDTO searchUserByNickname(String nickname){
        List<User> existingUser = userRepository.findByUserProfileNickname(nickname);
        if(existingUser.isEmpty()){
            return null;
        }
        User user = existingUser.get(0);
        UserProfile userProfile = user.getUserProfile();
        return UserProfileSearchDTO.builder()
                .userId(user.getId())
                .nickname(userProfile.getNickname())
                .profilePhoto(new ByteArrayResource(userProfile.getProfilePhoto().getBytes()))
                .build();
    }


}
