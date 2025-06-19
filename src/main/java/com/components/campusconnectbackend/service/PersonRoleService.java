package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.PersonRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface PersonRoleService {

    // Assign role to person
    PersonRoleDTO assignRoleToPerson(String personUid, Integer roleId);

    // Get all person-role assignments
    List<PersonRoleDTO> getAllPersonRoles();

    // Get roles for a person
    List<RoleDTO> getRolesForPerson(String personUid);

    // Get persons with a role
    List<PersonRoleDTO> getPersonsWithRole(Integer roleId);

    // Check if a person has a specific role
    boolean hasRole(String personUid, Integer roleId);

    // Check if a person has a role with hierarchy greater than or equal to level
    boolean hasRoleWithHierarchyGreaterThanEqual(String personUid, Integer hierarchyLevel);

    // Remove role from person
    void removeRoleFromPerson(String personUid, Integer roleId);

    // Remove all roles from person
    void removeAllRolesFromPerson(String personUid);

    // Remove role from all persons
    void removeRoleFromAllPersons(Integer roleId);
}