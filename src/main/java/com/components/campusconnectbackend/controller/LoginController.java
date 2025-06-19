package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.LoginDTO;
import com.components.campusconnectbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logins")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Create a new login
    @PostMapping
    public ResponseEntity<LoginDTO> createLogin(@RequestBody LoginDTO loginDTO) {
        try {
            LoginDTO createdLogin = loginService.createLogin(loginDTO);
            return new ResponseEntity<>(createdLogin, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all logins
    @GetMapping
    public ResponseEntity<List<LoginDTO>> getAllLogins() {
        List<LoginDTO> logins = loginService.getAllLogins();
        return new ResponseEntity<>(logins, HttpStatus.OK);
    }

    // Get login by username
    @GetMapping("/{username}")
    public ResponseEntity<LoginDTO> getLoginByUsername(@PathVariable String username) {
        return loginService.getLoginByUsername(username)
                .map(loginDTO -> new ResponseEntity<>(loginDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get login by person UID
    @GetMapping("/person/{uid}")
    public ResponseEntity<LoginDTO> getLoginByUid(@PathVariable String uid) {
        return loginService.getLoginByUid(uid)
                .map(loginDTO -> new ResponseEntity<>(loginDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update login
    @PutMapping("/{username}")
    public ResponseEntity<LoginDTO> updateLogin(@PathVariable String username, @RequestBody LoginDTO loginDTO) {
        try {
            LoginDTO updatedLogin = loginService.updateLogin(username, loginDTO);
            return new ResponseEntity<>(updatedLogin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update password
    @PatchMapping("/{username}/password")
    public ResponseEntity<LoginDTO> updatePassword(
            @PathVariable String username,
            @RequestBody Map<String, String> passwordMap) {
        try {
            String newPassword = passwordMap.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            LoginDTO updatedLogin = loginService.updatePassword(username, newPassword);
            return new ResponseEntity<>(updatedLogin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete login
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteLogin(@PathVariable String username) {
        if (loginService.existsByUsername(username)) {
            loginService.deleteLogin(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Authenticate
    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean authenticated = loginService.authenticate(username, password);
        return new ResponseEntity<>(authenticated, HttpStatus.OK);
    }

    // Check if username exists
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = loginService.existsByUsername(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // Check if person has login
    @GetMapping("/exists/person/{uid}")
    public ResponseEntity<Boolean> existsByUid(@PathVariable String uid) {
        boolean exists = loginService.existsByUid(uid);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}