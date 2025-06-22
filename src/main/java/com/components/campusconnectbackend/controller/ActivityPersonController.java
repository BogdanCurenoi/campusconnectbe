package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.ActivityPersonDTO;
import com.components.campusconnectbackend.service.ActivityPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-persons")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class ActivityPersonController {

    private final ActivityPersonService activityPersonService;

    @Autowired
    public ActivityPersonController(ActivityPersonService activityPersonService) {
        this.activityPersonService = activityPersonService;
    }

    // Register person for activity
    @PostMapping("/{personUid}/activities/{activityId}")
    public ResponseEntity<ActivityPersonDTO> registerPersonForActivity(
            @PathVariable String personUid,
            @PathVariable Integer activityId,
            @RequestParam(defaultValue = "false") Boolean attended) {
        try {
            ActivityPersonDTO activityPersonDTO = activityPersonService.registerPersonForActivity(personUid, activityId, attended);
            return new ResponseEntity<>(activityPersonDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all activity-person registrations
    @GetMapping
    public ResponseEntity<List<ActivityPersonDTO>> getAllActivityPersonRegistrations() {
        List<ActivityPersonDTO> registrations = activityPersonService.getAllActivityPersonRegistrations();
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get all activities for a person
    @GetMapping("/{personUid}/activities")
    public ResponseEntity<List<ActivityPersonDTO>> getActivitiesForPerson(@PathVariable String personUid) {
        List<ActivityPersonDTO> activities = activityPersonService.getActivitiesForPerson(personUid);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get all persons registered for an activity
    @GetMapping("/activities/{activityId}/persons")
    public ResponseEntity<List<ActivityPersonDTO>> getPersonsForActivity(@PathVariable Integer activityId) {
        List<ActivityPersonDTO> persons = activityPersonService.getPersonsForActivity(activityId);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Check if a person is registered for an activity
    @GetMapping("/{personUid}/activities/{activityId}/registered")
    public ResponseEntity<Boolean> isPersonRegisteredForActivity(
            @PathVariable String personUid,
            @PathVariable Integer activityId) {
        boolean isRegistered = activityPersonService.isPersonRegisteredForActivity(personUid, activityId);
        return new ResponseEntity<>(isRegistered, HttpStatus.OK);
    }

    // Update attendance status
    @PutMapping("/{personUid}/activities/{activityId}/attendance")
    public ResponseEntity<ActivityPersonDTO> updateAttendanceStatus(
            @PathVariable String personUid,
            @PathVariable Integer activityId,
            @RequestParam Boolean attended) {
        try {
            ActivityPersonDTO updatedRegistration = activityPersonService.updateAttendanceStatus(personUid, activityId, attended);
            return new ResponseEntity<>(updatedRegistration, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get registrations by attendance status
    @GetMapping("/attendance/{attended}")
    public ResponseEntity<List<ActivityPersonDTO>> getRegistrationsByAttendance(@PathVariable Boolean attended) {
        List<ActivityPersonDTO> registrations = activityPersonService.getRegistrationsByAttendance(attended);
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get activities for a person by attendance status
    @GetMapping("/{personUid}/activities/attendance/{attended}")
    public ResponseEntity<List<ActivityPersonDTO>> getActivitiesForPersonByAttendance(
            @PathVariable String personUid,
            @PathVariable Boolean attended) {
        List<ActivityPersonDTO> activities = activityPersonService.getActivitiesForPersonByAttendance(personUid, attended);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get persons for an activity by attendance status
    @GetMapping("/activities/{activityId}/persons/attendance/{attended}")
    public ResponseEntity<List<ActivityPersonDTO>> getPersonsForActivityByAttendance(
            @PathVariable Integer activityId,
            @PathVariable Boolean attended) {
        List<ActivityPersonDTO> persons = activityPersonService.getPersonsForActivityByAttendance(activityId, attended);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Remove person from activity
    @DeleteMapping("/{personUid}/activities/{activityId}")
    public ResponseEntity<Void> removePersonFromActivity(
            @PathVariable String personUid,
            @PathVariable Integer activityId) {
        try {
            activityPersonService.removePersonFromActivity(personUid, activityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Remove all activities for a person
    @DeleteMapping("/{personUid}/activities")
    public ResponseEntity<Void> removeAllActivitiesForPerson(@PathVariable String personUid) {
        activityPersonService.removeAllActivitiesForPerson(personUid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Remove all persons from an activity
    @DeleteMapping("/activities/{activityId}/persons")
    public ResponseEntity<Void> removeAllPersonsFromActivity(@PathVariable Integer activityId) {
        activityPersonService.removeAllPersonsFromActivity(activityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get activity count for a person
    @GetMapping("/{personUid}/activities/count")
    public ResponseEntity<Long> getActivityCountForPerson(@PathVariable String personUid) {
        long count = activityPersonService.getActivityCountForPerson(personUid);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Get activity count for a person by attendance status
    @GetMapping("/{personUid}/activities/count/attendance/{attended}")
    public ResponseEntity<Long> getActivityCountForPersonByAttendance(
            @PathVariable String personUid,
            @PathVariable Boolean attended) {
        long count = activityPersonService.getActivityCountForPersonByAttendance(personUid, attended);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Get person count for an activity
    @GetMapping("/activities/{activityId}/persons/count")
    public ResponseEntity<Long> getPersonCountForActivity(@PathVariable Integer activityId) {
        long count = activityPersonService.getPersonCountForActivity(activityId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Get person count for an activity by attendance status
    @GetMapping("/activities/{activityId}/persons/count/attendance/{attended}")
    public ResponseEntity<Long> getPersonCountForActivityByAttendance(
            @PathVariable Integer activityId,
            @PathVariable Boolean attended) {
        long count = activityPersonService.getPersonCountForActivityByAttendance(activityId, attended);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}