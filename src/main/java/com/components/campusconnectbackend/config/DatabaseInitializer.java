package com.components.campusconnectbackend.config;

import com.components.campusconnectbackend.domain.Role;
import com.components.campusconnectbackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public DatabaseInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Initialize default roles if they don't exist
        initializeRoles();
    }

    private void initializeRoles() {
        // Check if UNSIGNED role exists
        Optional<Role> unsignedRole = roleRepository.findByName("UNSIGNED");
        if (unsignedRole.isEmpty()) {
            // Create UNSIGNED role
            Role newRole = new Role();
            // Use field assignment instead of setters
            newRole.setName("UNSIGNED");
            newRole.setHierarchy(1); // Lowest hierarchy level
            roleRepository.save(newRole);
            System.out.println("Created default UNSIGNED role");
        }

        // Add other default roles as needed
    }
}