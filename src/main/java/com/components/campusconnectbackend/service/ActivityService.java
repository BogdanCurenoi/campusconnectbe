package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.ActivityDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService {

    // Create
    ActivityDTO createActivity(ActivityDTO activityDTO);

    // Create with file upload
    ActivityDTO createActivityWithPhoto(String name, String description, LocalDate date, Integer hours,
                                        Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                                        Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                                        String personUid, MultipartFile photo) throws IOException;

    // Read
    List<ActivityDTO> getAllActivities();
    List<ActivityDTO> getAllActivitiesOrderedByDate(boolean ascending);
    Optional<ActivityDTO> getActivityById(Integer id);
    List<ActivityDTO> searchActivitiesByName(String name);
    List<ActivityDTO> getActivitiesByDepartment(Integer departmentId);
    List<ActivityDTO> getActivitiesByPerson(String personUid);
    List<ActivityDTO> getActivitiesByDate(LocalDate date);
    List<ActivityDTO> getActivitiesBetweenDates(LocalDate startDate, LocalDate endDate);
    List<ActivityDTO> getActivitiesByObligatory(Boolean obligatory);
    List<ActivityDTO> getActivitiesByVotingEligibilityOnly(Boolean votingEligibilityOnly);
    List<ActivityDTO> getActivitiesByVotingProgress(Boolean votingProgress);
    List<ActivityDTO> getUpcomingActivities();
    List<ActivityDTO> getPastActivities();
    byte[] getActivityPhoto(Integer id);

    // Update
    ActivityDTO updateActivity(Integer id, ActivityDTO activityDTO);
    ActivityDTO updateActivityWithPhoto(Integer id, String name, String description, LocalDate date, Integer hours,
                                        Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                                        Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                                        String personUid, MultipartFile photo) throws IOException;

    // Delete
    void deleteActivity(Integer id);

    // Check if exists
    boolean existsById(Integer id);
}