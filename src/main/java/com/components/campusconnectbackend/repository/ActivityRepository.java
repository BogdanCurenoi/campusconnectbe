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

    // Find activities ordered by date (newest first)
    List<Activity> findAllByOrderByDateDesc();

    // Find activities ordered by date (oldest first)
    List<Activity> findAllByOrderByDateAsc();

    // Find upcoming activities (future dates)
    List<Activity> findByDateAfterOrderByDateAsc(LocalDate currentDate);

    // Find past activities
    List<Activity> findByDateBeforeOrderByDateDesc(LocalDate currentDate);
}