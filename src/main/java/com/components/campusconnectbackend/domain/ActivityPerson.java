package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "CC_ACTIVITY_PERSON")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPerson {

    @EmbeddedId
    private ActivityPersonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("personUid")
    @JoinColumn(name = "AA_UID")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("activityId")
    @JoinColumn(name = "AA_AID")
    private Activity activity;

    @Column(name = "AA_ATTENDED")
    private Boolean attended;

    // Composite primary key class for ActivityPerson
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityPersonId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "AA_UID")
        private String personUid;

        @Column(name = "AA_AID")
        private Integer activityId;
    }
}