package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private boolean success;
    private String uid;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String message;

    // Constructor for success response
    public AuthResponseDTO(boolean success, String uid, String name, String surname, String email, String username) {
        this.success = success;
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
    }

    // Constructor for error response
    public AuthResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}