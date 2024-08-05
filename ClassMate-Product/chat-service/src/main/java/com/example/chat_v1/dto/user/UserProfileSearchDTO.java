package com.example.chat_v1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileSearchDTO {
    private Long userId;
    private String nickname;
    private ByteArrayResource profilePhoto;
}
