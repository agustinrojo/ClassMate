package com.classmate.forum_service.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhotoDTO {
    private Long photoId;
    private String originalFilename;
    private String contentType;
    private Long size;
}
