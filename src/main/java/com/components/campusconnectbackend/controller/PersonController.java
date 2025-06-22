package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.PersonDTO;
import com.components.campusconnectbackend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Create a new person
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        PersonDTO createdPerson = personService.createPerson(personDTO);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    // Get all persons
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get person by ID
    @GetMapping("/{uid}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable String uid) {
        return personService.getPersonById(uid)
                .map(personDTO -> new ResponseEntity<>(personDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get person by email
    @GetMapping("/email")
    public ResponseEntity<PersonDTO> getPersonByEmail(@RequestParam String email) {
        return personService.getPersonByEmail(email)
                .map(personDTO -> new ResponseEntity<>(personDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get persons by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<PersonDTO>> getPersonsByDepartment(@PathVariable Integer departmentId) {
        List<PersonDTO> persons = personService.getPersonsByDepartment(departmentId);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by college
    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCollege(@PathVariable Integer collegeId) {
        List<PersonDTO> persons = personService.getPersonsByCollege(collegeId);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Update person
    @PutMapping("/{uid}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable String uid, @RequestBody PersonDTO personDTO) {
        try {
            PersonDTO updatedPerson = personService.updatePerson(uid, personDTO);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete person
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deletePerson(@PathVariable String uid) {
        if (personService.existsById(uid)) {
            personService.deletePerson(uid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // NEW ENDPOINTS FOR CP_HOURS FUNCTIONALITY

    // Get persons by exact CP_HOURS
    @GetMapping("/cp-hours/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHours(@PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCpHours(hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by CP_HOURS range
    @GetMapping("/cp-hours/range")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHoursRange(
            @RequestParam Integer minHours,
            @RequestParam Integer maxHours) {
        List<PersonDTO> persons = personService.getPersonsByCpHoursRange(minHours, maxHours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons with CP_HOURS greater than specified value
    @GetMapping("/cp-hours/greater-than/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHoursGreaterThan(@PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCpHoursGreaterThan(hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons with CP_HOURS less than specified value
    @GetMapping("/cp-hours/less-than/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHoursLessThan(@PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCpHoursLessThan(hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons with CP_HOURS greater than or equal to specified value
    @GetMapping("/cp-hours/greater-than-equal/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHoursGreaterThanEqual(@PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCpHoursGreaterThanEqual(hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons with CP_HOURS less than or equal to specified value
    @GetMapping("/cp-hours/less-than-equal/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCpHoursLessThanEqual(@PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCpHoursLessThanEqual(hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by department and exact CP_HOURS
    @GetMapping("/department/{departmentId}/cp-hours/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByDepartmentAndCpHours(
            @PathVariable Integer departmentId,
            @PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByDepartmentAndCpHours(departmentId, hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by college and exact CP_HOURS
    @GetMapping("/college/{collegeId}/cp-hours/{hours}")
    public ResponseEntity<List<PersonDTO>> getPersonsByCollegeAndCpHours(
            @PathVariable Integer collegeId,
            @PathVariable Integer hours) {
        List<PersonDTO> persons = personService.getPersonsByCollegeAndCpHours(collegeId, hours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by department and CP_HOURS range
    @GetMapping("/department/{departmentId}/cp-hours/range")
    public ResponseEntity<List<PersonDTO>> getPersonsByDepartmentAndCpHoursRange(
            @PathVariable Integer departmentId,
            @RequestParam Integer minHours,
            @RequestParam Integer maxHours) {
        List<PersonDTO> persons = personService.getPersonsByDepartmentAndCpHoursRange(departmentId, minHours, maxHours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Get persons by college and CP_HOURS range
    @GetMapping("/college/{collegeId}/cp-hours/range")
    public ResponseEntity<List<PersonDTO>> getPersonsByCollegeAndCpHoursRange(
            @PathVariable Integer collegeId,
            @RequestParam Integer minHours,
            @RequestParam Integer maxHours) {
        List<PersonDTO> persons = personService.getPersonsByCollegeAndCpHoursRange(collegeId, minHours, maxHours);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }
}