package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Login;
import com.components.campusconnectbackend.domain.Person;
import com.components.campusconnectbackend.dto.LoginDTO;
import com.components.campusconnectbackend.repository.LoginRepository;
import com.components.campusconnectbackend.repository.PersonRepository;
import com.components.campusconnectbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(
            LoginRepository loginRepository,
            PersonRepository personRepository) {
        this.loginRepository = loginRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public LoginDTO createLogin(LoginDTO loginDTO) {
        // Check if the username already exists
        if (loginRepository.existsById(loginDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if the person exists
        if (!personRepository.existsById(loginDTO.getUid())) {
            throw new RuntimeException("Person with UID: " + loginDTO.getUid() + " not found");
        }

        // Check if the person already has a login
        if (loginRepository.existsByUid(loginDTO.getUid())) {
            throw new RuntimeException("Person already has a login");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(loginDTO.getPassword());

        // Create and save login
        Login login = new Login();
        login.setUsername(loginDTO.getUsername());
        login.setPassword(hashedPassword);
        login.setUid(loginDTO.getUid());

        Login savedLogin = loginRepository.save(login);

        // Return DTO without password
        return convertToDTO(savedLogin, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoginDTO> getAllLogins() {
        return loginRepository.findAll().stream()
                .map(login -> convertToDTO(login, false)) // Don't include passwords
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoginDTO> getLoginByUsername(String username) {
        return loginRepository.findById(username)
                .map(login -> convertToDTO(login, false)); // Don't include password
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoginDTO> getLoginByUid(String uid) {
        return loginRepository.findByUid(uid)
                .map(login -> convertToDTO(login, false)); // Don't include password
    }

    @Override
    public LoginDTO updateLogin(String username, LoginDTO loginDTO) {
        Login login = loginRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Login with username: " + username + " not found"));

        // If username is being changed, check if the new username is available
        if (!login.getUsername().equals(loginDTO.getUsername()) &&
                loginRepository.existsById(loginDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // If UID is being changed, verify that the person exists and doesn't already have a login
        if (!login.getUid().equals(loginDTO.getUid())) {
            if (!personRepository.existsById(loginDTO.getUid())) {
                throw new RuntimeException("Person with UID: " + loginDTO.getUid() + " not found");
            }

            Optional<Login> existingLoginForPerson = loginRepository.findByUid(loginDTO.getUid());
            if (existingLoginForPerson.isPresent() && !existingLoginForPerson.get().getUsername().equals(username)) {
                throw new RuntimeException("Person already has a login");
            }
        }

        // Update the login (except password)
        login.setUsername(loginDTO.getUsername());
        login.setUid(loginDTO.getUid());

        // Update password if provided
        if (loginDTO.getPassword() != null && !loginDTO.getPassword().isEmpty()) {
            login.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
        }

        // Handle case of changing the username (primary key)
        if (!username.equals(loginDTO.getUsername())) {
            // First delete the old record
            loginRepository.deleteById(username);
            // Then save with the new username
            Login savedLogin = loginRepository.save(login);
            return convertToDTO(savedLogin, false);
        } else {
            // Simple update with same username
            Login savedLogin = loginRepository.save(login);
            return convertToDTO(savedLogin, false);
        }
    }

    @Override
    public LoginDTO updatePassword(String username, String newPassword) {
        Login login = loginRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Login with username: " + username + " not found"));

        // Hash and update the password
        login.setPassword(passwordEncoder.encode(newPassword));
        Login savedLogin = loginRepository.save(login);

        return convertToDTO(savedLogin, false);
    }

    @Override
    public void deleteLogin(String username) {
        loginRepository.deleteById(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        Optional<Login> loginOptional = loginRepository.findById(username);
        if (loginOptional.isPresent()) {
            Login login = loginOptional.get();
            return passwordEncoder.matches(password, login.getPassword());
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return loginRepository.existsById(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUid(String uid) {
        return loginRepository.existsByUid(uid);
    }

    // Helper methods to convert between entity and DTO
    private LoginDTO convertToDTO(Login login, boolean includePassword) {
        String personName = null;
        String personEmail = null;

        Person person = login.getPerson();
        if (person != null) {
            personName = person.getName() + " " + person.getSurname();
            personEmail = person.getEmail();
        }

        if (includePassword) {
            return new LoginDTO(
                    login.getUsername(),
                    login.getPassword(),
                    login.getUid(),
                    personName,
                    personEmail
            );
        } else {
            return new LoginDTO(
                    login.getUsername(),
                    login.getUid(),
                    personName,
                    personEmail
            );
        }
    }
}