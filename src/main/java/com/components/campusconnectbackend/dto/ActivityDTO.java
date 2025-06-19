package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Integer id;
    private String name;
    private String description;
    private byte[] photo;
    private LocalDate date;
    private Integer hours;
    private Boolean votingEligibilityOnly;
    private Boolean votingProgress;
    private Integer personNumber;
    private Boolean obligatory;
    private Integer cost;
    private String costBudget;
    private Integer departmentId;
    private String personUid;
    private String departmentName; // For convenience
    private String personName; // For convenience

    // Constructor without photo for cases where you don't want to transfer the image
    public ActivityDTO(Integer id, String name, String description, LocalDate date, Integer hours,
                       Boolean votingEligibilityOnly, Boolean votingProgress, Integer personNumber,
                       Boolean obligatory, Integer cost, String costBudget, Integer departmentId,
                       String personUid, String departmentName, String personName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.hours = hours;
        this.votingEligibilityOnly = votingEligibilityOnly;
        this.votingProgress = votingProgress;
        this.personNumber = personNumber;
        this.obligatory = obligatory;
        this.cost = cost;
        this.costBudget = costBudget;
        this.departmentId = departmentId;
        this.personUid = personUid;
        this.departmentName = departmentName;
        this.personName = personName;
    }
}