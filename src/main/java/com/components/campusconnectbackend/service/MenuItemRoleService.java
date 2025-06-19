package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.MenuItemDTO;
import com.components.campusconnectbackend.dto.MenuItemRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface MenuItemRoleService {

    // Assign role to menu item
    MenuItemRoleDTO assignRoleToMenuItem(Integer menuItemId, Integer roleId);

    // Get all menu item-role assignments
    List<MenuItemRoleDTO> getAllMenuItemRoles();

    // Get roles for a menu item
    List<RoleDTO> getRolesForMenuItem(Integer menuItemId);

    // Get menu items for a role
    List<MenuItemDTO> getMenuItemsForRole(Integer roleId);

    // Check if a menu item has a specific role
    boolean hasRole(Integer menuItemId, Integer roleId);

    // Get menu items for a user based on their roles
    List<MenuItemDTO> getMenuItemsForUser(String personUid);

    // Remove role from menu item
    void removeRoleFromMenuItem(Integer menuItemId, Integer roleId);

    // Remove all roles from menu item
    void removeAllRolesFromMenuItem(Integer menuItemId);

    // Remove menu item from all roles
    void removeMenuItemFromAllRoles(Integer roleId);
}