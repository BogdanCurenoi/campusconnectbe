package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    // Create
    RoleDTO createRole(RoleDTO roleDTO);

    // Read
    List<RoleDTO> getAllRoles();
    List<RoleDTO> getAllRolesOrderedByHierarchy(boolean ascending);
    Optional<RoleDTO> getRoleById(Integer id);
    Optional<RoleDTO> getRoleByName(String name);
    List<RoleDTO> getRolesByHierarchy(Integer hierarchy);
    List<RoleDTO> getRolesWithHierarchyLessThanEqual(Integer hierarchy);
    List<RoleDTO> getRolesWithHierarchyGreaterThanEqual(Integer hierarchy);

    // Update
    RoleDTO updateRole(Integer id, RoleDTO roleDTO);

    // Delete
    void deleteRole(Integer id);

    // Check if exists
    boolean existsById(Integer id);
}