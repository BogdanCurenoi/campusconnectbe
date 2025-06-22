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

    // New methods for CP_HOURS functionality
    List<PersonDTO> getPersonsByCpHours(Integer cpHours);

    List<PersonDTO> getPersonsByCpHoursRange(Integer minHours, Integer maxHours);

    List<PersonDTO> getPersonsByCpHoursGreaterThan(Integer hours);

    List<PersonDTO> getPersonsByCpHoursLessThan(Integer hours);

    List<PersonDTO> getPersonsByCpHoursGreaterThanEqual(Integer hours);

    List<PersonDTO> getPersonsByCpHoursLessThanEqual(Integer hours);

    // Combined queries for CP_HOURS with department/college
    List<PersonDTO> getPersonsByDepartmentAndCpHours(Integer departmentId, Integer cpHours);

    List<PersonDTO> getPersonsByCollegeAndCpHours(Integer collegeId, Integer cpHours);

    List<PersonDTO> getPersonsByDepartmentAndCpHoursRange(Integer departmentId, Integer minHours, Integer maxHours);

    List<PersonDTO> getPersonsByCollegeAndCpHoursRange(Integer collegeId, Integer minHours, Integer maxHours);
}