package com.example.Security.dto.user.profile;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePhotoUpdateDTO {
    @Nullable
    private Long photoIdToRemove;

    @Nullable
    private MultipartFile photoToAdd;
}
