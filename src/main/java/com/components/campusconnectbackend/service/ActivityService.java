package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.ActivityDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService {

    ActivityDTO createActivity(ActivityDTO activityDTO);

    ActivityDTO createActivityWithPhoto(String name, String description, LocalDate date, Integer hours,
                                        Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                                        Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                                        String personUid, MultipartFile photo) throws IOException;

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

    ActivityDTO updateActivity(Integer id, ActivityDTO activityDTO);

    void deleteActivity(Integer id);

    boolean existsById(Integer id);

    // NEW METHODS: Activity status management
    List<ActivityDTO> getActivitiesByActive(Boolean active);

    List<ActivityDTO> getActiveActivitiesOrderedByDate(boolean ascending);

    List<ActivityDTO> getActiveActivitiesByDepartment(Integer departmentId);

    List<ActivityDTO> getActiveActivitiesByPerson(String personUid);

    List<ActivityDTO> getActiveActivitiesByDate(LocalDate date);

    List<ActivityDTO> getActiveActivitiesBetweenDates(LocalDate startDate, LocalDate endDate);

    List<ActivityDTO> getActiveActivitiesByObligatory(Boolean obligatory);

    List<ActivityDTO> getActiveActivitiesByVotingEligibilityOnly(Boolean votingEligibilityOnly);

    List<ActivityDTO> getActiveActivitiesByVotingProgress(Boolean votingProgress);

    List<ActivityDTO> getActiveUpcomingActivities();

    List<ActivityDTO> getActivePastActivities();

    ActivityDTO activateActivity(Integer id);

    ActivityDTO deactivateActivity(Integer id);
}