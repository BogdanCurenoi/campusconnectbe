package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findByEmail(String email);

    List<Person> findByDepartmentId(Integer departmentId);

    List<Person> findByCollegeId(Integer collegeId);
}