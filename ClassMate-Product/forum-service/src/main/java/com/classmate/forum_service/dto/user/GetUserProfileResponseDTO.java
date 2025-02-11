package com.classmate.forum_service.dto.user;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileResponseDTO {
    private Long userId;
    private String nickname;
    private String name;
    private ProfilePhotoDTO profilePhoto;
    private String description;
    private List<Long> forumsSubscribed;
    private Long likesAmmount;
    private Long dislikesAmmount;
}
