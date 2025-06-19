package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Role;
import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.repository.RoleRepository;
import com.components.campusconnectbackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRolesOrderedByHierarchy(boolean ascending) {
        List<Role> roles;
        if (ascending) {
            roles = roleRepository.findAllByOrderByHierarchyAsc();
        } else {
            roles = roleRepository.findAllByOrderByHierarchyDesc();
        }
        return roles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> getRoleById(Integer id) {
        return roleRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> getRoleByName(String name) {
        return roleRepository.findByName(name)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getRolesByHierarchy(Integer hierarchy) {
        return roleRepository.findByHierarchy(hierarchy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getRolesWithHierarchyLessThanEqual(Integer hierarchy) {
        return roleRepository.findByHierarchyLessThanEqual(hierarchy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getRolesWithHierarchyGreaterThanEqual(Integer hierarchy) {
        return roleRepository.findByHierarchyGreaterThanEqual(hierarchy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {
        if (roleRepository.existsById(id)) {
            Role role = convertToEntity(roleDTO);
            role.setId(id); // Ensure the correct ID is set
            Role updatedRole = roleRepository.save(role);
            return convertToDTO(updatedRole);
        } else {
            throw new RuntimeException("Role with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return roleRepository.existsById(id);
    }

    // Helper methods to convert between DTO and Entity
    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getHierarchy()
        );
    }

    private Role convertToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        // When creating a new role, the ID might be null (auto-incremented)
        if (roleDTO.getId() != null) {
            role.setId(roleDTO.getId());
        }
        role.setName(roleDTO.getName());
        role.setHierarchy(roleDTO.getHierarchy());
        return role;
    }
}