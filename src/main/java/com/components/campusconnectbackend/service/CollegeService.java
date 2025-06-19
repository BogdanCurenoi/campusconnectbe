package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.CollegeDTO;

import java.util.List;
import java.util.Optional;

public interface CollegeService {

    // Create
    CollegeDTO createCollege(CollegeDTO collegeDTO);

    // Read
    List<CollegeDTO> getAllColleges();
    Optional<CollegeDTO> getCollegeById(Integer id);
    Optional<CollegeDTO> getCollegeByName(String name);
    List<CollegeDTO> searchCollegesByName(String namePart);

    // Update
    CollegeDTO updateCollege(Integer id, CollegeDTO collegeDTO);

    // Delete
    void deleteCollege(Integer id);

    // Check if exists
    boolean existsById(Integer id);
}