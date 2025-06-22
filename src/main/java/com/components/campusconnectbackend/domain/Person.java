package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_PERSON")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "CP_UID")
    private String uid;

    @Column(name = "CP_NAME")
    private String name;

    @Column(name = "CP_SURNAME")
    private String surname;

    @Column(name = "CP_EMAIL")
    private String email;

    @Column(name = "CP_DEPARTMENT_ID")
    private Integer departmentId;

    @Column(name = "CP_COLLEGE_ID")
    private Integer collegeId;

    @Column(name = "CP_PROFILE_PICTURE")
    private byte[] profilePicture;

    @Column(name = "CP_HOURS")
    private Integer cpHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CP_DEPARTMENT_ID", referencedColumnName = "CD_ID", insertable = false, updatable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CP_COLLEGE_ID", referencedColumnName = "CCO_ID", insertable = false, updatable = false)
    private College college;
}