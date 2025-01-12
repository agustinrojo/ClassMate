package com.classmate.comment_service.dto.user.userReputation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReputationChangeDTO {
    private Long userId;
    private ReputationAction action;
}
