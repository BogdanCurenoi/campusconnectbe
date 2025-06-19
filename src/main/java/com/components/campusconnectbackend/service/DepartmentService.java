package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.DepartmentDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    // Create
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    // Read
    List<DepartmentDTO> getAllDepartments();
    Optional<DepartmentDTO> getDepartmentById(Integer id);
    Optional<DepartmentDTO> getDepartmentByName(String name);
    List<DepartmentDTO> searchDepartmentsByName(String namePart);
    List<DepartmentDTO> getDepartmentsByCollege(Integer collegeId);

    // Update
    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDTO);

    // Delete
    void deleteDepartment(Integer id);

    // Check if exists
    boolean existsById(Integer id);
}