package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.ActivityDTO;
import com.components.campusconnectbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // Create a new activity
    @PostMapping
    public ResponseEntity<ActivityDTO> createActivity(@RequestBody ActivityDTO activityDTO) {
        ActivityDTO createdActivity = activityService.createActivity(activityDTO);
        return new ResponseEntity<>(createdActivity, HttpStatus.CREATED);
    }

    // Create a new activity with photo
    @PostMapping("/with-photo")
    public ResponseEntity<ActivityDTO> createActivityWithPhoto(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("hours") Integer hours,
            @RequestParam(value = "votingEligibilityOnly", required = false) Boolean votingEligibilityOnly,
            @RequestParam(value = "votingProgress", required = false) Boolean votingProgress,
            @RequestParam(value = "personNumber", required = false) Integer personNumber,
            @RequestParam(value = "obligatory", required = false) Boolean obligatory,
            @RequestParam(value = "cost", required = false) Integer cost,
            @RequestParam(value = "costBudget", required = false) String costBudget,
            @RequestParam(value = "departmentId", required = false) Integer departmentId,
            @RequestParam(value = "personUid", required = false) String personUid,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            ActivityDTO createdActivity = activityService.createActivityWithPhoto(
                    name, description, date, hours, votingEligibilityOnly, votingProgress,
                    personNumber, obligatory, cost, costBudget, departmentId, personUid, photo);
            return new ResponseEntity<>(createdActivity, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all activities
    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {
        List<ActivityDTO> activities = activityService.getAllActivities();
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get all activities ordered by date
    @GetMapping("/ordered")
    public ResponseEntity<List<ActivityDTO>> getAllActivitiesOrderedByDate(
            @RequestParam(defaultValue = "false") boolean ascending) {
        List<ActivityDTO> activities = activityService.getAllActivitiesOrderedByDate(ascending);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get upcoming activities
    @GetMapping("/upcoming")
    public ResponseEntity<List<ActivityDTO>> getUpcomingActivities() {
        List<ActivityDTO> activities = activityService.getUpcomingActivities();
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get past activities
    @GetMapping("/past")
    public ResponseEntity<List<ActivityDTO>> getPastActivities() {
        List<ActivityDTO> activities = activityService.getPastActivities();
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activity by ID
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Integer id) {
        return activityService.getActivityById(id)
                .map(activityDTO -> new ResponseEntity<>(activityDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get activity photo
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getActivityPhoto(@PathVariable Integer id) {
        byte[] photo = activityService.getActivityPhoto(id);
        if (photo != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your photo type
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Search activities by name
    @GetMapping("/search")
    public ResponseEntity<List<ActivityDTO>> searchActivitiesByName(@RequestParam String name) {
        List<ActivityDTO> activities = activityService.searchActivitiesByName(name);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByDepartment(@PathVariable Integer departmentId) {
        List<ActivityDTO> activities = activityService.getActivitiesByDepartment(departmentId);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by person
    @GetMapping("/person/{personUid}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByPerson(@PathVariable String personUid) {
        List<ActivityDTO> activities = activityService.getActivitiesByPerson(personUid);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ActivityDTO> activities = activityService.getActivitiesByDate(date);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities between dates
    @GetMapping("/date-range")
    public ResponseEntity<List<ActivityDTO>> getActivitiesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ActivityDTO> activities = activityService.getActivitiesBetweenDates(startDate, endDate);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by obligatory status
    @GetMapping("/obligatory/{obligatory}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByObligatory(@PathVariable Boolean obligatory) {
        List<ActivityDTO> activities = activityService.getActivitiesByObligatory(obligatory);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by voting eligibility only
    @GetMapping("/voting-eligibility/{votingEligibilityOnly}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByVotingEligibilityOnly(
            @PathVariable Boolean votingEligibilityOnly) {
        List<ActivityDTO> activities = activityService.getActivitiesByVotingEligibilityOnly(votingEligibilityOnly);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Get activities by voting progress
    @GetMapping("/voting-progress/{votingProgress}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByVotingProgress(
            @PathVariable Boolean votingProgress) {
        List<ActivityDTO> activities = activityService.getActivitiesByVotingProgress(votingProgress);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    // Update activity
    @PutMapping("/{id}")
    public ResponseEntity<ActivityDTO> updateActivity(@PathVariable Integer id, @RequestBody ActivityDTO activityDTO) {
        try {
            ActivityDTO updatedActivity = activityService.updateActivity(id, activityDTO);
            return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update activity with photo
    @PutMapping("/{id}/with-photo")
    public ResponseEntity<ActivityDTO> updateActivityWithPhoto(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("hours") Integer hours,
            @RequestParam(value = "votingEligibilityOnly", required = false) Boolean votingEligibilityOnly,
            @RequestParam(value = "votingProgress", required = false) Boolean votingProgress,
            @RequestParam(value = "personNumber", required = false) Integer personNumber,
            @RequestParam(value = "obligatory", required = false) Boolean obligatory,
            @RequestParam(value = "cost", required = false) Integer cost,
            @RequestParam(value = "costBudget", required = false) String costBudget,
            @RequestParam(value = "departmentId", required = false) Integer departmentId,
            @RequestParam(value = "personUid", required = false) String personUid,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            ActivityDTO updatedActivity = activityService.updateActivityWithPhoto(
                    id, name, description, date, hours, votingEligibilityOnly, votingProgress,
                    personNumber, obligatory, cost, costBudget, departmentId, personUid, photo);
            return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete activity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Integer id) {
        if (activityService.existsById(id)) {
            activityService.deleteActivity(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}