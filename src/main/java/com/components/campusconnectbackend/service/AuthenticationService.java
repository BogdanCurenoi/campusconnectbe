package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.AuthResponseDTO;
import com.components.campusconnectbackend.dto.LoginRequestDTO;
import com.components.campusconnectbackend.dto.RegisterRequestDTO;

public interface AuthenticationService {

    // Register a new user
    AuthResponseDTO register(RegisterRequestDTO registerRequest);

    // Login an existing user
    AuthResponseDTO login(LoginRequestDTO loginRequest);
}