package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Create a new role
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get all roles ordered by hierarchy
    @GetMapping("/ordered")
    public ResponseEntity<List<RoleDTO>> getAllRolesOrdered(@RequestParam(defaultValue = "true") boolean ascending) {
        List<RoleDTO> roles = roleService.getAllRolesOrderedByHierarchy(ascending);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id) {
        return roleService.getRoleById(id)
                .map(roleDTO -> new ResponseEntity<>(roleDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get role by name
    @GetMapping("/name")
    public ResponseEntity<RoleDTO> getRoleByName(@RequestParam String name) {
        return roleService.getRoleByName(name)
                .map(roleDTO -> new ResponseEntity<>(roleDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get roles by hierarchy level
    @GetMapping("/hierarchy/{level}")
    public ResponseEntity<List<RoleDTO>> getRolesByHierarchy(@PathVariable Integer level) {
        List<RoleDTO> roles = roleService.getRolesByHierarchy(level);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get roles with hierarchy level less than or equal to the specified level
    @GetMapping("/hierarchy/less-than-equal/{level}")
    public ResponseEntity<List<RoleDTO>> getRolesWithHierarchyLessThanEqual(@PathVariable Integer level) {
        List<RoleDTO> roles = roleService.getRolesWithHierarchyLessThanEqual(level);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get roles with hierarchy level greater than or equal to the specified level
    @GetMapping("/hierarchy/greater-than-equal/{level}")
    public ResponseEntity<List<RoleDTO>> getRolesWithHierarchyGreaterThanEqual(@PathVariable Integer level) {
        List<RoleDTO> roles = roleService.getRolesWithHierarchyGreaterThanEqual(level);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Update role
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        if (roleService.existsById(id)) {
            roleService.deleteRole(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}