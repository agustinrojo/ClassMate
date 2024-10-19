package com.example.Security.dto.user.profile;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileWithRoleDTO {
    private Long userId;
    private String nickname;
    private ProfilePhotoDTO profilePhoto;
    private String userType; // "Creator", "Admin", "Subscriber"
}
