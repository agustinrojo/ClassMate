package com.example.Security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    private String firstName;
    private String lastName;
    private String legajo;
    private String carrera;
    private String email;
    private String password;
}
