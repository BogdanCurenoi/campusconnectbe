package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRoleDTO {
    private Integer menuItemId;
    private String menuItemDescription;  // For convenience
    private String menuItemPath;         // For convenience
    private Integer roleId;
    private String roleName;             // For convenience
    private Integer roleHierarchy;       // For convenience

    // Constructor with only essential fields
    public MenuItemRoleDTO(Integer menuItemId, Integer roleId) {
        this.menuItemId = menuItemId;
        this.roleId = roleId;
    }
}