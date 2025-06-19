package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollegeRepository extends JpaRepository<College, Integer> {
    Optional<College> findByName(String name);
    List<College> findByNameContaining(String namePart);
}