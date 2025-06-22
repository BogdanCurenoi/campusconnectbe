package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.Activity;
import com.components.campusconnectbackend.domain.ActivityPerson;
import com.components.campusconnectbackend.domain.Person;
import com.components.campusconnectbackend.dto.ActivityPersonDTO;
import com.components.campusconnectbackend.repository.ActivityPersonRepository;
import com.components.campusconnectbackend.repository.ActivityRepository;
import com.components.campusconnectbackend.repository.PersonRepository;
import com.components.campusconnectbackend.service.ActivityPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityPersonServiceImpl implements ActivityPersonService {

    private final ActivityPersonRepository activityPersonRepository;
    private final PersonRepository personRepository;
    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityPersonServiceImpl(
            ActivityPersonRepository activityPersonRepository,
            PersonRepository personRepository,
            ActivityRepository activityRepository) {
        this.activityPersonRepository = activityPersonRepository;
        this.personRepository = personRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityPersonDTO registerPersonForActivity(String personUid, Integer activityId) {
        return registerPersonForActivity(personUid, activityId, false); // Default attendance to false
    }

    @Override
    public ActivityPersonDTO registerPersonForActivity(String personUid, Integer activityId, Boolean attended) {
        // Check if person and activity exist
        Person person = personRepository.findById(personUid)
                .orElseThrow(() -> new RuntimeException("Person with UID: " + personUid + " not found"));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity with ID: " + activityId + " not found"));

        // Check if the registration already exists
        if (activityPersonRepository.existsByIdPersonUidAndIdActivityId(personUid, activityId)) {
            throw new RuntimeException("Person is already registered for this activity");
        }

        // Create new ActivityPerson entity
        ActivityPerson activityPerson = new ActivityPerson();
        activityPerson.setId(new ActivityPerson.ActivityPersonId(personUid, activityId));
        activityPerson.setPerson(person);
        activityPerson.setActivity(activity);
        activityPerson.setAttended(attended != null ? attended : false);

        // Save and return
        ActivityPerson savedActivityPerson = activityPersonRepository.save(activityPerson);
        return convertToDTO(savedActivityPerson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getAllActivityPersonRegistrations() {
        return activityPersonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getActivitiesForPerson(String personUid) {
        return activityPersonRepository.findByIdPersonUid(personUid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getPersonsForActivity(Integer activityId) {
        return activityPersonRepository.findByIdActivityId(activityId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPersonRegisteredForActivity(String personUid, Integer activityId) {
        return activityPersonRepository.existsByIdPersonUidAndIdActivityId(personUid, activityId);
    }

    @Override
    public ActivityPersonDTO updateAttendanceStatus(String personUid, Integer activityId, Boolean attended) {
        ActivityPerson activityPerson = activityPersonRepository.findByIdPersonUidAndIdActivityId(personUid, activityId)
                .orElseThrow(() -> new RuntimeException("Registration not found for person: " + personUid + " and activity: " + activityId));

        activityPerson.setAttended(attended);
        ActivityPerson updatedActivityPerson = activityPersonRepository.save(activityPerson);
        return convertToDTO(updatedActivityPerson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getRegistrationsByAttendance(Boolean attended) {
        return activityPersonRepository.findByAttended(attended).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getActivitiesForPersonByAttendance(String personUid, Boolean attended) {
        return activityPersonRepository.findByIdPersonUidAndAttended(personUid, attended).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityPersonDTO> getPersonsForActivityByAttendance(Integer activityId, Boolean attended) {
        return activityPersonRepository.findByIdActivityIdAndAttended(activityId, attended).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removePersonFromActivity(String personUid, Integer activityId) {
        // Check if the registration exists
        if (!activityPersonRepository.existsByIdPersonUidAndIdActivityId(personUid, activityId)) {
            throw new RuntimeException("Person is not registered for this activity");
        }

        activityPersonRepository.deleteByIdPersonUidAndIdActivityId(personUid, activityId);
    }

    @Override
    public void removeAllActivitiesForPerson(String personUid) {
        activityPersonRepository.deleteByIdPersonUid(personUid);
    }

    @Override
    public void removeAllPersonsFromActivity(Integer activityId) {
        activityPersonRepository.deleteByIdActivityId(activityId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActivityCountForPerson(String personUid) {
        return activityPersonRepository.countByIdPersonUid(personUid);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActivityCountForPersonByAttendance(String personUid, Boolean attended) {
        return activityPersonRepository.countByIdPersonUidAndAttended(personUid, attended);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPersonCountForActivity(Integer activityId) {
        return activityPersonRepository.countByIdActivityId(activityId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPersonCountForActivityByAttendance(Integer activityId, Boolean attended) {
        return activityPersonRepository.countByIdActivityIdAndAttended(activityId, attended);
    }

    // Helper method to convert between entity and DTO
    private ActivityPersonDTO convertToDTO(ActivityPerson activityPerson) {
        Person person = activityPerson.getPerson();
        Activity activity = activityPerson.getActivity();

        return new ActivityPersonDTO(
                person.getUid(),
                person.getName() + " " + person.getSurname(),
                person.getEmail(),
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activityPerson.getAttended()
        );
    }
}