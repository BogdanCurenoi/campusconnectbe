package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String username;
    private String password; // In a real app, consider not transferring this in DTO
    private String uid;
    private String personName; // For convenience
    private String personEmail; // For convenience

    // Constructor without sensitive fields for responses
    public LoginDTO(String username, String uid, String personName, String personEmail) {
        this.username = username;
        this.uid = uid;
        this.personName = personName;
        this.personEmail = personEmail;
    }
}