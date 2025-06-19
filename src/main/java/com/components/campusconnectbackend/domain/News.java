package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_NEWS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NW_ID")
    private Integer id;

    @Column(name = "NW_TITLE")
    private String title;

    @Column(name = "NW_BODY", length = 1000)
    private String body;

    @Column(name = "NW_PHOTO")
    @Lob
    private byte[] photo;

    @Column(name = "NW_DEPT_ID")
    private Integer departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NW_DEPT_ID", referencedColumnName = "CD_ID", insertable = false, updatable = false)
    private Department department;
}