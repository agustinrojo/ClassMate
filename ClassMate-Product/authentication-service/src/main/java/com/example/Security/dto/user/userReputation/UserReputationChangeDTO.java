package com.example.Security.dto.user.userReputation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReputationChangeDTO {
    private Long userId;
    private ReputationAction action;
}
