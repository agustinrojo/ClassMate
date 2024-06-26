package com.example.Security.dto.user.profile;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long userId;
    private String nickname;
    private ProfilePhotoDTO profilePhoto;
    private String description;
}
