package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CC_MENU_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ME_ID")
    private Integer id;

    @Column(name = "ME_DESCRIPTION")
    private String description;

    @Column(name = "ME_PATH")
    private String path;

    @Column(name = "ME_INDEX")
    private Integer index;
}