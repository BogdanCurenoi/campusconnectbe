package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.ActivityPersonDTO;

import java.util.List;

public interface ActivityPersonService {

    // Register a person for an activity
    ActivityPersonDTO registerPersonForActivity(String personUid, Integer activityId);

    // Register a person for an activity with attendance status
    ActivityPersonDTO registerPersonForActivity(String personUid, Integer activityId, Boolean attended);

    // Get all activity-person registrations
    List<ActivityPersonDTO> getAllActivityPersonRegistrations();

    // Get all activities for a specific person
    List<ActivityPersonDTO> getActivitiesForPerson(String personUid);

    // Get all persons registered for a specific activity
    List<ActivityPersonDTO> getPersonsForActivity(Integer activityId);

    // Check if a person is registered for an activity
    boolean isPersonRegisteredForActivity(String personUid, Integer activityId);

    // Update attendance status for a person in an activity
    ActivityPersonDTO updateAttendanceStatus(String personUid, Integer activityId, Boolean attended);

    // Get registrations by attendance status
    List<ActivityPersonDTO> getRegistrationsByAttendance(Boolean attended);

    // Get activities for a person by attendance status
    List<ActivityPersonDTO> getActivitiesForPersonByAttendance(String personUid, Boolean attended);

    // Get persons for an activity by attendance status
    List<ActivityPersonDTO> getPersonsForActivityByAttendance(Integer activityId, Boolean attended);

    // Remove a person from an activity
    void removePersonFromActivity(String personUid, Integer activityId);

    // Remove all registrations for a person
    void removeAllActivitiesForPerson(String personUid);

    // Remove all registrations for an activity
    void removeAllPersonsFromActivity(Integer activityId);

    // Get activity count for a person
    long getActivityCountForPerson(String personUid);

    // Get activity count for a person by attendance status
    long getActivityCountForPersonByAttendance(String personUid, Boolean attended);

    // Get person count for an activity
    long getPersonCountForActivity(Integer activityId);

    // Get person count for an activity by attendance status
    long getPersonCountForActivityByAttendance(Integer activityId, Boolean attended);
}