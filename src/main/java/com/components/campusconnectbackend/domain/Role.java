package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CR_ID")
    private Integer id;

    @Column(name = "CR_NAME")
    private String name;

    @Column(name = "CR_HIERARCHY")
    private Integer hierarchy;

}