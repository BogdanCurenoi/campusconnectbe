package com.components.campusconnectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "CC_MENU_ITEMS_ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRole {

    @EmbeddedId
    private MenuItemRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemId")
    @JoinColumn(name = "MR_MENU_ITEM_ID")
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "MR_ROLE_ID")
    private Role role;

    // Composite primary key class for MenuItemRole
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuItemRoleId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "MR_MENU_ITEM_ID")
        private Integer menuItemId;

        @Column(name = "MR_ROLE_ID")
        private Integer roleId;
    }
}