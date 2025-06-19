package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "CC_PERSON_ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRole {

    @EmbeddedId
    private PersonRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("personUid")
    @JoinColumn(name = "PR_PERSON_UID")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "PR_ROLE_ID")
    private Role role;

    // Composite primary key class for PersonRole
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonRoleId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "PR_PERSON_UID")
        private String personUid;

        @Column(name = "PR_ROLE_ID")
        private Integer roleId;
    }
}