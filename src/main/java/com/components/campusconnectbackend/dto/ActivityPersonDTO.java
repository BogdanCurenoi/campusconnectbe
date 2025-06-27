package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPersonDTO {

    private String personUid;
    private String personFullName;
    private String personEmail;
    private Integer activityId;
    private String activityName;
    private String activityDescription;
    private LocalDate activityDate;
    private Boolean attended;

    // Constructor for simpler creation when you have all the basic info
    public ActivityPersonDTO(String personUid, Integer activityId, Boolean attended) {
        this.personUid = personUid;
        this.activityId = activityId;
        this.attended = attended;
    }
}