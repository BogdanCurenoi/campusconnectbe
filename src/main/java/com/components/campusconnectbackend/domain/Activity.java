package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "CC_ACTIVITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AC_ID")
    private Integer id;

    @Column(name = "AC_NAME")
    private String name;

    @Column(name = "AC_DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "AC_PHOTO")
    @Lob
    private byte[] photo;

    @Column(name = "AC_DATE")
    private LocalDate date;

    @Column(name = "AC_HOURS")
    private Integer hours;

    @Column(name = "AC_VOTING_ELIGIBILITY_ONLY")
    private Boolean votingEligibilityOnly;

    @Column(name = "AC_VOTING_PROGRESS")
    private Boolean votingProgress;

    @Column(name = "AC_PERSON_NUMBER")
    private Integer personNumber;

    @Column(name = "AC_OBLIGATORY")
    private Boolean obligatory;

    @Column(name = "AC_COST")
    private Integer cost;

    @Column(name = "AC_COST_BUGET")
    private String costBudget;

    @Column(name = "AC_DEPARTMENT_ID")
    private Integer departmentId;

    @Column(name = "AC_PERSON_UID")
    private String personUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AC_DEPARTMENT_ID", referencedColumnName = "CD_ID", insertable = false, updatable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AC_PERSON_UID", referencedColumnName = "CP_UID", insertable = false, updatable = false)
    private Person person;
}