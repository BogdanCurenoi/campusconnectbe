package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.ActivityPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityPersonRepository extends JpaRepository<ActivityPerson, ActivityPerson.ActivityPersonId> {

    // Find all activity-person assignments for a specific person
    List<ActivityPerson> findByIdPersonUid(String personUid);

    // Find all activity-person assignments for a specific activity
    List<ActivityPerson> findByIdActivityId(Integer activityId);

    // Find specific activity-person assignment
    Optional<ActivityPerson> findByIdPersonUidAndIdActivityId(String personUid, Integer activityId);

    // Check if a person is registered for an activity
    boolean existsByIdPersonUidAndIdActivityId(String personUid, Integer activityId);

    // Find all activity-person assignments by attendance status
    List<ActivityPerson> findByAttended(Boolean attended);

    // Find activity-person assignments for a specific person by attendance status
    List<ActivityPerson> findByIdPersonUidAndAttended(String personUid, Boolean attended);

    // Find activity-person assignments for a specific activity by attendance status
    List<ActivityPerson> findByIdActivityIdAndAttended(Integer activityId, Boolean attended);

    // Delete operations
    void deleteByIdPersonUid(String personUid);
    void deleteByIdActivityId(Integer activityId);
    void deleteByIdPersonUidAndIdActivityId(String personUid, Integer activityId);

    // Count operations
    long countByIdActivityId(Integer activityId);
    long countByIdActivityIdAndAttended(Integer activityId, Boolean attended);
    long countByIdPersonUid(String personUid);
    long countByIdPersonUidAndAttended(String personUid, Boolean attended);
}