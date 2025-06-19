package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private Integer departmentId; // Optional, can be null
    private Integer collegeId; // Optional, can be null
}