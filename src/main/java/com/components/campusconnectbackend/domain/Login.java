package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_LOGIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    @Id
    @Column(name = "CL_USERNAME")
    private String username;

    @Column(name = "CL_PASSWORD")
    private String password;

    @Column(name = "CL_UID")
    private String uid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CL_UID", referencedColumnName = "CP_UID", insertable = false, updatable = false)
    private Person person;
}