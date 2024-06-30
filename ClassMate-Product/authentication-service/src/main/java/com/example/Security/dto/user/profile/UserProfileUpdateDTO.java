package com.example.Security.dto.user.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    private String nickname;
    private ProfilePhotoUpdateDTO profilePhotoUpdateDTO;
    private String description;
}
