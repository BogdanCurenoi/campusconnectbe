package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Activity;
import com.components.campusconnectbackend.dto.ActivityDTO;
import com.components.campusconnectbackend.repository.ActivityRepository;
import com.components.campusconnectbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityDTO createActivity(ActivityDTO activityDTO) {
        Activity activity = convertToEntity(activityDTO);
        // Set default active status to true for new activities
        if (activity.getActive() == null) {
            activity.setActive(true);
        }
        Activity savedActivity = activityRepository.save(activity);
        return convertToDTO(savedActivity);
    }

    @Override
    public ActivityDTO createActivityWithPhoto(String name, String description, LocalDate date, Integer hours,
                                               Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                                               Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                                               String personUid, MultipartFile photo) throws IOException {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(description);
        activity.setDate(date);
        activity.setHours(hours);
        activity.setVotingEligibilityOnly(votingEligibilityOnly != null ? votingEligibilityOnly : false);
        activity.setVotingProgress(votingProgress != null ? votingProgress : false);
        activity.setPersonNumber(personNumber);
        activity.setObligatory(obligatory != null ? obligatory : false);
        activity.setCost(cost);
        activity.setCostBudget(costBudget);
        activity.setDepartmentId(departmentId);
        activity.setPersonUid(personUid);
        // Set default active status to true for new activities
        activity.setActive(true);

        if (photo != null && !photo.isEmpty()) {
            activity.setPhoto(photo.getBytes());
        }

        Activity savedActivity = activityRepository.save(activity);
        return convertToDTO(savedActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getAllActivitiesOrderedByDate(boolean ascending) {
        List<Activity> activities;
        if (ascending) {
            activities = activityRepository.findAllByOrderByDateAsc();
        } else {
            activities = activityRepository.findAllByOrderByDateDesc();
        }
        return activities.stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityDTO> getActivityById(Integer id) {
        return activityRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> searchActivitiesByName(String name) {
        return activityRepository.findByNameContaining(name).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByDepartment(Integer departmentId) {
        return activityRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByPerson(String personUid) {
        return activityRepository.findByPersonUid(personUid).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByDate(LocalDate date) {
        return activityRepository.findByDate(date).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return activityRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByObligatory(Boolean obligatory) {
        return activityRepository.findByObligatory(obligatory).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByVotingEligibilityOnly(Boolean votingEligibilityOnly) {
        return activityRepository.findByVotingEligibilityOnly(votingEligibilityOnly).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByVotingProgress(Boolean votingProgress) {
        return activityRepository.findByVotingProgress(votingProgress).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getUpcomingActivities() {
        LocalDate today = LocalDate.now();
        return activityRepository.findByDateAfterOrderByDateAsc(today).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getPastActivities() {
        LocalDate today = LocalDate.now();
        return activityRepository.findByDateBeforeOrderByDateDesc(today).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getActivityPhoto(Integer id) {
        return activityRepository.findById(id)
                .map(Activity::getPhoto)
                .orElse(null);
    }

    @Override
    public ActivityDTO updateActivity(Integer id, ActivityDTO activityDTO) {
        if (activityRepository.existsById(id)) {
            Activity activity = convertToEntity(activityDTO);
            activity.setId(id); // Ensure the correct ID is set

            // Get existing activity to preserve or clear photo based on DTO
            activityRepository.findById(id).ifPresent(existingActivity -> {
                // If the DTO doesn't include photo data, preserve the existing photo
                if (activityDTO.getPhoto() == null && existingActivity.getPhoto() != null) {
                    activity.setPhoto(existingActivity.getPhoto());
                }
                // If the DTO doesn't include active status, preserve the existing active status
                if (activityDTO.getActive() == null) {
                    activity.setActive(existingActivity.getActive());
                }
            });

            Activity updatedActivity = activityRepository.save(activity);
            return convertToDTO(updatedActivity);
        } else {
            throw new RuntimeException("Activity with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteActivity(Integer id) {
        activityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return activityRepository.existsById(id);
    }

    // NEW METHODS: Activity status management
    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByActive(Boolean active) {
        return activityRepository.findByActive(active).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesOrderedByDate(boolean ascending) {
        List<Activity> activities;
        if (ascending) {
            activities = activityRepository.findByActiveOrderByDateAsc(true);
        } else {
            activities = activityRepository.findByActiveOrderByDateDesc(true);
        }
        return activities.stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByDepartment(Integer departmentId) {
        return activityRepository.findByActiveAndDepartmentId(true, departmentId).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByPerson(String personUid) {
        return activityRepository.findByActiveAndPersonUid(true, personUid).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByDate(LocalDate date) {
        return activityRepository.findByActiveAndDate(true, date).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return activityRepository.findByActiveAndDateBetween(true, startDate, endDate).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByObligatory(Boolean obligatory) {
        return activityRepository.findByActiveAndObligatory(true, obligatory).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByVotingEligibilityOnly(Boolean votingEligibilityOnly) {
        return activityRepository.findByActiveAndVotingEligibilityOnly(true, votingEligibilityOnly).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveActivitiesByVotingProgress(Boolean votingProgress) {
        return activityRepository.findByActiveAndVotingProgress(true, votingProgress).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActiveUpcomingActivities() {
        LocalDate today = LocalDate.now();
        return activityRepository.findByActiveAndDateAfterOrderByDateAsc(true, today).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivePastActivities() {
        LocalDate today = LocalDate.now();
        return activityRepository.findByActiveAndDateBeforeOrderByDateDesc(true, today).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityDTO activateActivity(Integer id) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setActive(true);
                    Activity updatedActivity = activityRepository.save(activity);
                    return convertToDTO(updatedActivity);
                })
                .orElseThrow(() -> new RuntimeException("Activity with ID: " + id + " not found"));
    }

    @Override
    public ActivityDTO deactivateActivity(Integer id) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setActive(false);
                    Activity updatedActivity = activityRepository.save(activity);
                    return convertToDTO(updatedActivity);
                })
                .orElseThrow(() -> new RuntimeException("Activity with ID: " + id + " not found"));
    }

    // Helper methods to convert between entity and DTO
    private ActivityDTO convertToDTO(Activity activity) {
        String departmentName = null;
        if (activity.getDepartment() != null) {
            departmentName = activity.getDepartment().getName();
        }

        String personName = null;
        if (activity.getPerson() != null) {
            personName = activity.getPerson().getName() + " " + activity.getPerson().getSurname();
        }

        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getPhoto(),
                activity.getDate(),
                activity.getHours(),
                activity.getVotingEligibilityOnly(),
                activity.getVotingProgress(),
                activity.getPersonNumber(),
                activity.getObligatory(),
                activity.getCost(),
                activity.getCostBudget(),
                activity.getDepartmentId(),
                activity.getPersonUid(),
                activity.getActive(), // NEW FIELD
                departmentName,
                personName
        );
    }

    private ActivityDTO convertToDTOWithoutPhoto(Activity activity) {
        String departmentName = null;
        if (activity.getDepartment() != null) {
            departmentName = activity.getDepartment().getName();
        }

        String personName = null;
        if (activity.getPerson() != null) {
            personName = activity.getPerson().getName() + " " + activity.getPerson().getSurname();
        }

        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getDate(),
                activity.getHours(),
                activity.getVotingEligibilityOnly(),
                activity.getVotingProgress(),
                activity.getPersonNumber(),
                activity.getObligatory(),
                activity.getCost(),
                activity.getCostBudget(),
                activity.getDepartmentId(),
                activity.getPersonUid(),
                activity.getActive(), // NEW FIELD
                departmentName,
                personName
        );
    }

    private Activity convertToEntity(ActivityDTO activityDTO) {
        Activity activity = new Activity();
        activity.setId(activityDTO.getId());
        activity.setName(activityDTO.getName());
        activity.setDescription(activityDTO.getDescription());
        activity.setDate(activityDTO.getDate());
        activity.setHours(activityDTO.getHours());
        activity.setVotingEligibilityOnly(activityDTO.getVotingEligibilityOnly() != null ? activityDTO.getVotingEligibilityOnly() : false);
        activity.setVotingProgress(activityDTO.getVotingProgress() != null ? activityDTO.getVotingProgress() : false);
        activity.setPersonNumber(activityDTO.getPersonNumber());
        activity.setObligatory(activityDTO.getObligatory() != null ? activityDTO.getObligatory() : false);
        activity.setCost(activityDTO.getCost());
        activity.setCostBudget(activityDTO.getCostBudget());
        activity.setDepartmentId(activityDTO.getDepartmentId());
        activity.setPersonUid(activityDTO.getPersonUid());
        activity.setActive(activityDTO.getActive() != null ? activityDTO.getActive() : true); // NEW FIELD - default to true

        // Handle photo data exactly like PersonServiceImpl does
        if (activityDTO.getPhoto() != null) {
            activity.setPhoto(activityDTO.getPhoto());
        }

        return activity;
    }
}