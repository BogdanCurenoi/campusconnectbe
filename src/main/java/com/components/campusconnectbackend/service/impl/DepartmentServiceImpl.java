package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Department;
import com.components.campusconnectbackend.dto.DepartmentDTO;
import com.components.campusconnectbackend.repository.DepartmentRepository;
import com.components.campusconnectbackend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> searchDepartmentsByName(String namePart) {
        return departmentRepository.findByNameContaining(namePart).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getDepartmentsByCollege(Integer collegeId) {
        return departmentRepository.findByCollegeId(collegeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDTO) {
        if (departmentRepository.existsById(id)) {
            Department department = convertToEntity(departmentDTO);
            department.setId(id); // Ensure the correct ID is set
            Department updatedDepartment = departmentRepository.save(department);
            return convertToDTO(updatedDepartment);
        } else {
            throw new RuntimeException("Department with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return departmentRepository.existsById(id);
    }

    // Helper methods to convert between DTO and Entity
    private DepartmentDTO convertToDTO(Department department) {
        String collegeName = null;
        if (department.getCollege() != null) {
            collegeName = department.getCollege().getName();
        }

        return new DepartmentDTO(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getHexColor(),
                department.getCollegeId(),
                collegeName
        );
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        // When creating a new department, the ID might be null (auto-incremented)
        if (departmentDTO.getId() != null) {
            department.setId(departmentDTO.getId());
        }
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());
        department.setHexColor(departmentDTO.getHexColor());
        department.setCollegeId(departmentDTO.getCollegeId());
        return department;
    }
}