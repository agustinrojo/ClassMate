package com.example.Security.dto.auth;

import com.example.Security.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResp {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
