package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Person;
import com.components.campusconnectbackend.dto.PersonDTO;
import com.components.campusconnectbackend.repository.PersonRepository;
import com.components.campusconnectbackend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = convertToEntity(personDTO);
        Person savedPerson = personRepository.save(person);
        return convertToDTO(savedPerson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> getPersonById(String uid) {
        return personRepository.findById(uid)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> getPersonByEmail(String email) {
        return personRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByDepartment(Integer departmentId) {
        return personRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCollege(Integer collegeId) {
        return personRepository.findByCollegeId(collegeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO updatePerson(String uid, PersonDTO personDTO) {
        if (personRepository.existsById(uid)) {
            Person person = convertToEntity(personDTO);
            person.setUid(uid); // Ensure the correct ID is set
            Person updatedPerson = personRepository.save(person);
            return convertToDTO(updatedPerson);
        } else {
            throw new RuntimeException("Person with UID: " + uid + " not found");
        }
    }

    @Override
    public void deletePerson(String uid) {
        personRepository.deleteById(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String uid) {
        return personRepository.existsById(uid);
    }

    // Helper methods to convert between DTO and Entity
    private PersonDTO convertToDTO(Person person) {
        String departmentName = null;
        if (person.getDepartment() != null) {
            departmentName = person.getDepartment().getName();
        }

        String collegeName = null;
        if (person.getCollege() != null) {
            collegeName = person.getCollege().getName();
        }

        return new PersonDTO(
                person.getUid(),
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                person.getDepartmentId(),
                person.getCollegeId(),
                person.getProfilePicture(),
                person.getCpHours(),
                departmentName,
                collegeName
        );
    }

    private Person convertToEntity(PersonDTO personDTO) {
        Person person = new Person();
        person.setUid(personDTO.getUid());
        person.setName(personDTO.getName());
        person.setSurname(personDTO.getSurname());
        person.setEmail(personDTO.getEmail());
        person.setDepartmentId(personDTO.getDepartmentId());
        person.setCollegeId(personDTO.getCollegeId());
        person.setProfilePicture(personDTO.getProfilePicture());
        person.setCpHours(personDTO.getCpHours());
        return person;
    }

    // New methods for CP_HOURS functionality
    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHours(Integer cpHours) {
        return personRepository.findByCpHours(cpHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHoursRange(Integer minHours, Integer maxHours) {
        return personRepository.findByCpHoursBetween(minHours, maxHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHoursGreaterThan(Integer hours) {
        return personRepository.findByCpHoursGreaterThan(hours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHoursLessThan(Integer hours) {
        return personRepository.findByCpHoursLessThan(hours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHoursGreaterThanEqual(Integer hours) {
        return personRepository.findByCpHoursGreaterThanEqual(hours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCpHoursLessThanEqual(Integer hours) {
        return personRepository.findByCpHoursLessThanEqual(hours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByDepartmentAndCpHours(Integer departmentId, Integer cpHours) {
        return personRepository.findByDepartmentIdAndCpHours(departmentId, cpHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCollegeAndCpHours(Integer collegeId, Integer cpHours) {
        return personRepository.findByCollegeIdAndCpHours(collegeId, cpHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByDepartmentAndCpHoursRange(Integer departmentId, Integer minHours, Integer maxHours) {
        return personRepository.findByDepartmentIdAndCpHoursBetween(departmentId, minHours, maxHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> getPersonsByCollegeAndCpHoursRange(Integer collegeId, Integer minHours, Integer maxHours) {
        return personRepository.findByCollegeIdAndCpHoursBetween(collegeId, minHours, maxHours).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}