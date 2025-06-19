package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Person;
import com.components.campusconnectbackend.domain.PersonRole;
import com.components.campusconnectbackend.domain.Role;
import com.components.campusconnectbackend.dto.PersonRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.repository.PersonRepository;
import com.components.campusconnectbackend.repository.PersonRoleRepository;
import com.components.campusconnectbackend.repository.RoleRepository;
import com.components.campusconnectbackend.service.PersonRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonRoleServiceImpl implements PersonRoleService {

    private final PersonRoleRepository personRoleRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PersonRoleServiceImpl(
            PersonRoleRepository personRoleRepository,
            PersonRepository personRepository,
            RoleRepository roleRepository) {
        this.personRoleRepository = personRoleRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public PersonRoleDTO assignRoleToPerson(String personUid, Integer roleId) {
        // Check if person and role exist
        Person person = personRepository.findById(personUid)
                .orElseThrow(() -> new RuntimeException("Person with UID: " + personUid + " not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role with ID: " + roleId + " not found"));

        // Check if the assignment already exists
        if (personRoleRepository.existsByIdPersonUidAndIdRoleId(personUid, roleId)) {
            throw new RuntimeException("Person already has this role assigned");
        }

        // Create new PersonRole entity
        PersonRole personRole = new PersonRole();
        personRole.setId(new PersonRole.PersonRoleId(personUid, roleId));
        personRole.setPerson(person);
        personRole.setRole(role);

        // Save and return
        PersonRole savedPersonRole = personRoleRepository.save(personRole);
        return convertToDTO(savedPersonRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonRoleDTO> getAllPersonRoles() {
        return personRoleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getRolesForPerson(String personUid) {
        return personRoleRepository.findByIdPersonUid(personUid).stream()
                .map(PersonRole::getRole)
                .map(this::convertRoleToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonRoleDTO> getPersonsWithRole(Integer roleId) {
        return personRoleRepository.findByIdRoleId(roleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(String personUid, Integer roleId) {
        return personRoleRepository.existsByIdPersonUidAndIdRoleId(personUid, roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRoleWithHierarchyGreaterThanEqual(String personUid, Integer hierarchyLevel) {
        List<RoleDTO> roles = getRolesForPerson(personUid);
        return roles.stream()
                .anyMatch(role -> role.getHierarchy() >= hierarchyLevel);
    }

    @Override
    public void removeRoleFromPerson(String personUid, Integer roleId) {
        // Check if the assignment exists
        if (!personRoleRepository.existsByIdPersonUidAndIdRoleId(personUid, roleId)) {
            throw new RuntimeException("Person does not have this role assigned");
        }

        personRoleRepository.deleteByIdPersonUidAndIdRoleId(personUid, roleId);
    }

    @Override
    public void removeAllRolesFromPerson(String personUid) {
        personRoleRepository.deleteByIdPersonUid(personUid);
    }

    @Override
    public void removeRoleFromAllPersons(Integer roleId) {
        personRoleRepository.deleteByIdRoleId(roleId);
    }

    // Helper methods to convert between entities and DTOs
    private PersonRoleDTO convertToDTO(PersonRole personRole) {
        Person person = personRole.getPerson();
        Role role = personRole.getRole();

        return new PersonRoleDTO(
                person.getUid(),
                person.getName() + " " + person.getSurname(),
                person.getEmail(),
                role.getId(),
                role.getName(),
                role.getHierarchy()
        );
    }

    private RoleDTO convertRoleToDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getHierarchy()
        );
    }
}