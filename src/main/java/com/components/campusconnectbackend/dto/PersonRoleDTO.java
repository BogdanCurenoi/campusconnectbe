package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRoleDTO {
    private String personUid;
    private String personName;  // For convenience
    private String personEmail; // For convenience
    private Integer roleId;
    private String roleName;    // For convenience
    private Integer roleHierarchy; // For convenience

    // Constructor with only the essential fields
    public PersonRoleDTO(String personUid, Integer roleId) {
        this.personUid = personUid;
        this.roleId = roleId;
    }
}