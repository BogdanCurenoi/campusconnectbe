package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_DEPARTMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_ID")
    private Integer id;

    @Column(name = "CD_NAME")
    private String name;

    @Column(name = "CD_DESCRIPTION")
    private String description;

    @Column(name = "CD_HEX_COLOR")
    private String hexColor;

    @Column(name = "CD_COLLEGE_ID")
    private Integer collegeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_COLLEGE_ID", referencedColumnName = "CCO_ID", insertable = false, updatable = false)
    private College college;
}