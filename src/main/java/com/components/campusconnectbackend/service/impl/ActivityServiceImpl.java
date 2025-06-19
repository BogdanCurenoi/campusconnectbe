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
                // If photo is explicitly null in DTO, remove it (user deleted it)
                if (activityDTO.getPhoto() == null) {
                    activity.setPhoto(null);
                } else if (activityDTO.getPhoto().length == 0) {
                    // Empty byte array also means remove photo
                    activity.setPhoto(null);
                } else {
                    // Photo has content, use it
                    activity.setPhoto(activityDTO.getPhoto());
                }
            });

            Activity updatedActivity = activityRepository.save(activity);
            return convertToDTO(updatedActivity);
        } else {
            throw new RuntimeException("Activity with ID: " + id + " not found");
        }
    }

    @Override
    public ActivityDTO updateActivityWithPhoto(Integer id, String name, String description, LocalDate date, Integer hours,
                                               Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                                               Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                                               String personUid, MultipartFile photo) throws IOException {
        if (activityRepository.existsById(id)) {
            Activity activity = activityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Activity with ID: " + id + " not found"));

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

            if (photo != null && !photo.isEmpty()) {
                activity.setPhoto(photo.getBytes());
            }

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

        // Handle photo data exactly like PersonServiceImpl does
        if (activityDTO.getPhoto() != null) {
            activity.setPhoto(activityDTO.getPhoto());
        }

        return activity;
    }
}