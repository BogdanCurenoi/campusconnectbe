package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.CollegeDTO;
import com.components.campusconnectbackend.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class CollegeController {

    private final CollegeService collegeService;

    @Autowired
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    // Create a new college
    @PostMapping
    public ResponseEntity<CollegeDTO> createCollege(@RequestBody CollegeDTO collegeDTO) {
        CollegeDTO createdCollege = collegeService.createCollege(collegeDTO);
        return new ResponseEntity<>(createdCollege, HttpStatus.CREATED);
    }

    // Get all colleges
    @GetMapping
    public ResponseEntity<List<CollegeDTO>> getAllColleges() {
        List<CollegeDTO> colleges = collegeService.getAllColleges();
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }

    // Get college by ID
    @GetMapping("/{id}")
    public ResponseEntity<CollegeDTO> getCollegeById(@PathVariable Integer id) {
        return collegeService.getCollegeById(id)
                .map(collegeDTO -> new ResponseEntity<>(collegeDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get college by name
    @GetMapping("/name")
    public ResponseEntity<CollegeDTO> getCollegeByName(@RequestParam String name) {
        return collegeService.getCollegeByName(name)
                .map(collegeDTO -> new ResponseEntity<>(collegeDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Search colleges by name
    @GetMapping("/search")
    public ResponseEntity<List<CollegeDTO>> searchCollegesByName(@RequestParam String namePart) {
        List<CollegeDTO> colleges = collegeService.searchCollegesByName(namePart);
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }

    // Update college
    @PutMapping("/{id}")
    public ResponseEntity<CollegeDTO> updateCollege(@PathVariable Integer id, @RequestBody CollegeDTO collegeDTO) {
        try {
            CollegeDTO updatedCollege = collegeService.updateCollege(id, collegeDTO);
            return new ResponseEntity<>(updatedCollege, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete college
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollege(@PathVariable Integer id) {
        if (collegeService.existsById(id)) {
            collegeService.deleteCollege(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}