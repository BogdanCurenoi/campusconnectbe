package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Login;
import com.components.campusconnectbackend.domain.Person;
import com.components.campusconnectbackend.domain.PersonRole;
import com.components.campusconnectbackend.domain.Role;
import com.components.campusconnectbackend.dto.AuthResponseDTO;
import com.components.campusconnectbackend.dto.LoginRequestDTO;
import com.components.campusconnectbackend.dto.RegisterRequestDTO;
import com.components.campusconnectbackend.repository.LoginRepository;
import com.components.campusconnectbackend.repository.PersonRepository;
import com.components.campusconnectbackend.repository.PersonRoleRepository;
import com.components.campusconnectbackend.repository.RoleRepository;
import com.components.campusconnectbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PersonRepository personRepository;
    private final LoginRepository loginRepository;
    private final RoleRepository roleRepository;
    private final PersonRoleRepository personRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecureRandom random;

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    @Autowired
    public AuthenticationServiceImpl(
            PersonRepository personRepository,
            LoginRepository loginRepository,
            RoleRepository roleRepository,
            PersonRoleRepository personRoleRepository) {
        this.personRepository = personRepository;
        this.loginRepository = loginRepository;
        this.roleRepository = roleRepository;
        this.personRoleRepository = personRoleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.random = new SecureRandom();
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        // Check if username already exists
        if (loginRepository.existsByUsername(registerRequest.getUsername())) {
            return new AuthResponseDTO(false, "Username already exists");
        }

        // Check if email already exists in person table
        Optional<Person> existingPerson = personRepository.findByEmail(registerRequest.getEmail());
        if (existingPerson.isPresent()) {
            return new AuthResponseDTO(false, "Email already registered");
        }

        try {
            // Generate unique UID
            String uid = generateUID();

            // Create and save person
            Person person = new Person();
            person.setUid(uid);
            person.setName(registerRequest.getName());
            person.setSurname(registerRequest.getSurname());
            person.setEmail(registerRequest.getEmail());
            person.setDepartmentId(registerRequest.getDepartmentId());
            person.setCollegeId(registerRequest.getCollegeId());

            Person savedPerson = personRepository.save(person);

            // Create and save login
            Login login = new Login();
            login.setUsername(registerRequest.getUsername());
            login.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            login.setUid(uid);

            loginRepository.save(login);

            // Find the UNSIGNED role
            Optional<Role> unsignedRole = roleRepository.findByName("UNSIGNED");
            if (unsignedRole.isEmpty()) {
                throw new RuntimeException("UNSIGNED role not found");
            }

            // Create and save person-role
            PersonRole personRole = new PersonRole();
            personRole.setId(new PersonRole.PersonRoleId(uid, unsignedRole.get().getId()));
            personRole.setPerson(savedPerson);
            personRole.setRole(unsignedRole.get());

            personRoleRepository.save(personRole);

            // Return success response
            return new AuthResponseDTO(
                    true,
                    uid,
                    person.getName(),
                    person.getSurname(),
                    person.getEmail(),
                    login.getUsername()
            );
        } catch (Exception e) {
            return new AuthResponseDTO(false, "Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        // Find login by username
        Optional<Login> loginOptional = loginRepository.findById(loginRequest.getUsername());

        if (loginOptional.isEmpty()) {
            return new AuthResponseDTO(false, "Invalid username or password");
        }

        Login login = loginOptional.get();

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), login.getPassword())) {
            return new AuthResponseDTO(false, "Invalid username or password");
        }

        // Get person details
        Optional<Person> personOptional = personRepository.findById(login.getUid());

        if (personOptional.isEmpty()) {
            return new AuthResponseDTO(false, "User account is incomplete");
        }

        Person person = personOptional.get();

        // Return success response
        return new AuthResponseDTO(
                true,
                person.getUid(),
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                login.getUsername()
        );
    }

    // Helper method to generate a UID in the format aaaa-bbbb-cccc-dddd
    private String generateUID() {
        StringBuilder sb = new StringBuilder();
        for (int section = 0; section < 4; section++) {
            for (int i = 0; i < 4; i++) {
                int index = random.nextInt(CHARS.length());
                sb.append(CHARS.charAt(index));
            }
            if (section < 3) {
                sb.append('-');
            }
        }
        return sb.toString();
    }
}