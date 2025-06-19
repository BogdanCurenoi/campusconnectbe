package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    // Create
    PersonDTO createPerson(PersonDTO personDTO);

    // Read
    List<PersonDTO> getAllPersons();
    Optional<PersonDTO> getPersonById(String uid);
    Optional<PersonDTO> getPersonByEmail(String email);
    List<PersonDTO> getPersonsByDepartment(Integer departmentId);
    List<PersonDTO> getPersonsByCollege(Integer collegeId);

    // Update
    PersonDTO updatePerson(String uid, PersonDTO personDTO);

    // Delete
    void deletePerson(String uid);

    // Check if exists
    boolean existsById(String uid);
}