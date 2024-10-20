package com.classmate.forum_service.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanUserDeleteMemberEventDTO {
    private Long userIdToBan;
    private Long forumId;
}
