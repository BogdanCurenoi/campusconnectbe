package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.AuthResponseDTO;
import com.components.campusconnectbackend.dto.LoginRequestDTO;
import com.components.campusconnectbackend.dto.RegisterRequestDTO;
import com.components.campusconnectbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        AuthResponseDTO response = authenticationService.register(registerRequest);
        return response.isSuccess()
                ? new ResponseEntity<>(response, HttpStatus.CREATED)
                : new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authenticationService.login(loginRequest);
        return response.isSuccess()
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}