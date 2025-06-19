package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.College;
import com.components.campusconnectbackend.dto.CollegeDTO;
import com.components.campusconnectbackend.repository.CollegeRepository;
import com.components.campusconnectbackend.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeServiceImpl(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @Override
    public CollegeDTO createCollege(CollegeDTO collegeDTO) {
        College college = convertToEntity(collegeDTO);
        College savedCollege = collegeRepository.save(college);
        return convertToDTO(savedCollege);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeDTO> getAllColleges() {
        return collegeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDTO> getCollegeById(Integer id) {
        return collegeRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDTO> getCollegeByName(String name) {
        return collegeRepository.findByName(name)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeDTO> searchCollegesByName(String namePart) {
        return collegeRepository.findByNameContaining(namePart).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CollegeDTO updateCollege(Integer id, CollegeDTO collegeDTO) {
        if (collegeRepository.existsById(id)) {
            College college = convertToEntity(collegeDTO);
            college.setId(id); // Ensure the correct ID is set
            College updatedCollege = collegeRepository.save(college);
            return convertToDTO(updatedCollege);
        } else {
            throw new RuntimeException("College with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteCollege(Integer id) {
        collegeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return collegeRepository.existsById(id);
    }

    // Helper methods to convert between DTO and Entity
    private CollegeDTO convertToDTO(College college) {
        return new CollegeDTO(
                college.getId(),
                college.getName(),
                college.getAddress()
        );
    }

    private College convertToEntity(CollegeDTO collegeDTO) {
        College college = new College();
        // When creating a new college, the ID might be null (auto-incremented)
        if (collegeDTO.getId() != null) {
            college.setId(collegeDTO.getId());
        }
        college.setName(collegeDTO.getName());
        college.setAddress(collegeDTO.getAddress());
        return college;
    }
}