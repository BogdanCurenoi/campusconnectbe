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

    // New methods for CP_HOURS functionality
    List<Person> findByCpHours(Integer cpHours);

    List<Person> findByCpHoursBetween(Integer minHours, Integer maxHours);

    List<Person> findByCpHoursGreaterThan(Integer hours);

    List<Person> findByCpHoursLessThan(Integer hours);

    List<Person> findByCpHoursGreaterThanEqual(Integer hours);

    List<Person> findByCpHoursLessThanEqual(Integer hours);

    // Combined queries for CP_HOURS with department/college
    List<Person> findByDepartmentIdAndCpHours(Integer departmentId, Integer cpHours);

    List<Person> findByCollegeIdAndCpHours(Integer collegeId, Integer cpHours);

    List<Person> findByDepartmentIdAndCpHoursBetween(Integer departmentId, Integer minHours, Integer maxHours);

    List<Person> findByCollegeIdAndCpHoursBetween(Integer collegeId, Integer minHours, Integer maxHours);
}