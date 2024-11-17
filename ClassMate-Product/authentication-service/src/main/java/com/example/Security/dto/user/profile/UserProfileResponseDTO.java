package com.example.Security.dto.user.profile;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long userId;
    private String nickname;
    private String name;
    private ProfilePhotoDTO profilePhoto;
    private String description;
    private List<Long> forumsSubscribed;
    private List<Long> postsCreated;
}
