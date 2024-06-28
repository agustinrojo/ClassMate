package com.example.Security.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {
    private Long userId;
    private String nickname;
    private MultipartFile profilePhoto;
    private String description;
}
