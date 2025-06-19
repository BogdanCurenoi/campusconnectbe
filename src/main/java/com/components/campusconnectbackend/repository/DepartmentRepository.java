package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByName(String name);
    List<Department> findByNameContaining(String namePart);
    List<Department> findByCollegeId(Integer collegeId);
}