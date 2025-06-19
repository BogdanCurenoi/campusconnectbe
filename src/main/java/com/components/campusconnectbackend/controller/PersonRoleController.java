package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.PersonRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.service.PersonRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person-roles")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class PersonRoleController {

    private final PersonRoleService personRoleService;

    @Autowired
    public PersonRoleController(PersonRoleService personRoleService) {
        this.personRoleService = personRoleService;
    }

    // Assign role to person
    @PostMapping("/{personUid}/roles/{roleId}")
    public ResponseEntity<PersonRoleDTO> assignRoleToPerson(
            @PathVariable String personUid,
            @PathVariable Integer roleId) {
        try {
            PersonRoleDTO personRoleDTO = personRoleService.assignRoleToPerson(personUid, roleId);
            return new ResponseEntity<>(personRoleDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all person-role assignments
    @GetMapping
    public ResponseEntity<List<PersonRoleDTO>> getAllPersonRoles() {
        List<PersonRoleDTO> personRoles = personRoleService.getAllPersonRoles();
        return new ResponseEntity<>(personRoles, HttpStatus.OK);
    }

    // Get roles for a person
    @GetMapping("/{personUid}/roles")
    public ResponseEntity<List<RoleDTO>> getRolesForPerson(@PathVariable String personUid) {
        List<RoleDTO> roles = personRoleService.getRolesForPerson(personUid);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get persons with a role
    @GetMapping("/roles/{roleId}/persons")
    public ResponseEntity<List<PersonRoleDTO>> getPersonsWithRole(@PathVariable Integer roleId) {
        List<PersonRoleDTO> persons = personRoleService.getPersonsWithRole(roleId);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Check if a person has a specific role
    @GetMapping("/{personUid}/has-role/{roleId}")
    public ResponseEntity<Boolean> hasRole(
            @PathVariable String personUid,
            @PathVariable Integer roleId) {
        boolean hasRole = personRoleService.hasRole(personUid, roleId);
        return new ResponseEntity<>(hasRole, HttpStatus.OK);
    }

    // Check if a person has a role with hierarchy greater than or equal to level
    @GetMapping("/{personUid}/has-role-with-hierarchy-gte/{hierarchyLevel}")
    public ResponseEntity<Boolean> hasRoleWithHierarchyGreaterThanEqual(
            @PathVariable String personUid,
            @PathVariable Integer hierarchyLevel) {
        boolean hasRoleWithHierarchy = personRoleService.hasRoleWithHierarchyGreaterThanEqual(personUid, hierarchyLevel);
        return new ResponseEntity<>(hasRoleWithHierarchy, HttpStatus.OK);
    }

    // Remove role from person
    @DeleteMapping("/{personUid}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromPerson(
            @PathVariable String personUid,
            @PathVariable Integer roleId) {
        try {
            personRoleService.removeRoleFromPerson(personUid, roleId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Remove all roles from person
    @DeleteMapping("/{personUid}/roles")
    public ResponseEntity<Void> removeAllRolesFromPerson(@PathVariable String personUid) {
        personRoleService.removeAllRolesFromPerson(personUid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Remove role from all persons
    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromAllPersons(@PathVariable Integer roleId) {
        personRoleService.removeRoleFromAllPersons(roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}