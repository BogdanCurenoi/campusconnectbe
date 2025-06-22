package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    List<Activity> findByNameContaining(String name);

    List<Activity> findByDepartmentId(Integer departmentId);

    List<Activity> findByPersonUid(String personUid);

    List<Activity> findByDate(LocalDate date);

    List<Activity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Activity> findByObligatory(Boolean obligatory);

    List<Activity> findByVotingEligibilityOnly(Boolean votingEligibilityOnly);

    List<Activity> findByVotingProgress(Boolean votingProgress);

    // NEW METHODS: Find activities by active status
    List<Activity> findByActive(Boolean active);

    List<Activity> findByActiveAndDepartmentId(Boolean active, Integer departmentId);

    List<Activity> findByActiveAndPersonUid(Boolean active, String personUid);

    List<Activity> findByActiveAndDate(Boolean active, LocalDate date);

    List<Activity> findByActiveAndDateBetween(Boolean active, LocalDate startDate, LocalDate endDate);

    List<Activity> findByActiveAndObligatory(Boolean active, Boolean obligatory);

    List<Activity> findByActiveAndVotingEligibilityOnly(Boolean active, Boolean votingEligibilityOnly);

    List<Activity> findByActiveAndVotingProgress(Boolean active, Boolean votingProgress);

    // Find activities ordered by date (newest first)
    List<Activity> findAllByOrderByDateDesc();

    // Find activities ordered by date (oldest first)
    List<Activity> findAllByOrderByDateAsc();

    // NEW METHODS: Find active activities ordered by date
    List<Activity> findByActiveOrderByDateDesc(Boolean active);

    List<Activity> findByActiveOrderByDateAsc(Boolean active);

    // Find upcoming activities (future dates)
    List<Activity> findByDateAfterOrderByDateAsc(LocalDate currentDate);

    // Find past activities
    List<Activity> findByDateBeforeOrderByDateDesc(LocalDate currentDate);

    // NEW METHODS: Find upcoming/past activities filtered by active status
    List<Activity> findByActiveAndDateAfterOrderByDateAsc(Boolean active, LocalDate currentDate);

    List<Activity> findByActiveAndDateBeforeOrderByDateDesc(Boolean active, LocalDate currentDate);
}