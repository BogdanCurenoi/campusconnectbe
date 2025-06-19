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
}