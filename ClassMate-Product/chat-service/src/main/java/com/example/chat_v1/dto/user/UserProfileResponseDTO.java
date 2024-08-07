package com.example.chat_v1.dto.user;

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
