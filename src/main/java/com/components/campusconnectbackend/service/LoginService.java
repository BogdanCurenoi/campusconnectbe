package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.LoginDTO;

import java.util.List;
import java.util.Optional;

public interface LoginService {

    // Create
    LoginDTO createLogin(LoginDTO loginDTO);

    // Read
    List<LoginDTO> getAllLogins();
    Optional<LoginDTO> getLoginByUsername(String username);
    Optional<LoginDTO> getLoginByUid(String uid);

    // Update
    LoginDTO updateLogin(String username, LoginDTO loginDTO);
    LoginDTO updatePassword(String username, String newPassword);

    // Delete
    void deleteLogin(String username);

    // Authentication
    boolean authenticate(String username, String password);

    // Check if exists
    boolean existsByUsername(String username);
    boolean existsByUid(String uid);
}