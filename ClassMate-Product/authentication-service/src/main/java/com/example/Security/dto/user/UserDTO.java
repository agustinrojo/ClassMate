package com.example.Security.dto.user;

import com.example.Security.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String legajo;
    private String carrera;
    private Role role;
    private String email;
    private List<Long> forumsSubscribed;
    private List<Long> forumsCreated;
    private List<Long> forumsAdmin;
    private List<Long> chatroomIdsIn;
    private boolean isSynced;
}
