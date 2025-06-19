package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private String uid;
    private String name;
    private String surname;
    private String email;
    private Integer departmentId;
    private Integer collegeId;
    private byte[] profilePicture;
    private String departmentName; // Including department name for convenience
    private String collegeName; // Including college name for convenience

    // Constructor without profile picture
    public PersonDTO(String uid, String name, String surname, String email, Integer departmentId, Integer collegeId, String departmentName, String collegeName) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.departmentId = departmentId;
        this.collegeId = collegeId;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
    }
}