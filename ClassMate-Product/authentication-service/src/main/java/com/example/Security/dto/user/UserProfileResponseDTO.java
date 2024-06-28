package com.example.Security.dto.user;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long userId;
    private String nickname;
    private FileDTO profilePhoto;
    private String description;
}
