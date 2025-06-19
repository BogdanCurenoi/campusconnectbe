package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.MenuItemDTO;
import com.components.campusconnectbackend.dto.MenuItemRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.service.MenuItemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-item-roles")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class MenuItemRoleController {

    private final MenuItemRoleService menuItemRoleService;

    @Autowired
    public MenuItemRoleController(MenuItemRoleService menuItemRoleService) {
        this.menuItemRoleService = menuItemRoleService;
    }

    // Assign role to menu item
    @PostMapping("/{menuItemId}/roles/{roleId}")
    public ResponseEntity<MenuItemRoleDTO> assignRoleToMenuItem(
            @PathVariable Integer menuItemId,
            @PathVariable Integer roleId) {
        try {
            MenuItemRoleDTO menuItemRoleDTO = menuItemRoleService.assignRoleToMenuItem(menuItemId, roleId);
            return new ResponseEntity<>(menuItemRoleDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all menu item-role assignments
    @GetMapping
    public ResponseEntity<List<MenuItemRoleDTO>> getAllMenuItemRoles() {
        List<MenuItemRoleDTO> menuItemRoles = menuItemRoleService.getAllMenuItemRoles();
        return new ResponseEntity<>(menuItemRoles, HttpStatus.OK);
    }

    // Get roles for a menu item
    @GetMapping("/{menuItemId}/roles")
    public ResponseEntity<List<RoleDTO>> getRolesForMenuItem(@PathVariable Integer menuItemId) {
        List<RoleDTO> roles = menuItemRoleService.getRolesForMenuItem(menuItemId);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Get menu items for a role
    @GetMapping("/roles/{roleId}/menu-items")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsForRole(@PathVariable Integer roleId) {
        List<MenuItemDTO> menuItems = menuItemRoleService.getMenuItemsForRole(roleId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Get menu items for a user based on their roles
    @GetMapping("/user/{personUid}/menu-items")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsForUser(@PathVariable String personUid) {
        List<MenuItemDTO> menuItems = menuItemRoleService.getMenuItemsForUser(personUid);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Check if a menu item has a specific role
    @GetMapping("/{menuItemId}/has-role/{roleId}")
    public ResponseEntity<Boolean> hasRole(
            @PathVariable Integer menuItemId,
            @PathVariable Integer roleId) {
        boolean hasRole = menuItemRoleService.hasRole(menuItemId, roleId);
        return new ResponseEntity<>(hasRole, HttpStatus.OK);
    }

    // Remove role from menu item
    @DeleteMapping("/{menuItemId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromMenuItem(
            @PathVariable Integer menuItemId,
            @PathVariable Integer roleId) {
        try {
            menuItemRoleService.removeRoleFromMenuItem(menuItemId, roleId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Remove all roles from menu item
    @DeleteMapping("/{menuItemId}/roles")
    public ResponseEntity<Void> removeAllRolesFromMenuItem(@PathVariable Integer menuItemId) {
        menuItemRoleService.removeAllRolesFromMenuItem(menuItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Remove menu item from all roles
    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> removeMenuItemFromAllRoles(@PathVariable Integer roleId) {
        menuItemRoleService.removeMenuItemFromAllRoles(roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}