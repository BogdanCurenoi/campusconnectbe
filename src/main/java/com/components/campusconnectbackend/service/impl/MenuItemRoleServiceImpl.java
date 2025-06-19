package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.MenuItem;
import com.components.campusconnectbackend.domain.MenuItemRole;
import com.components.campusconnectbackend.domain.PersonRole;
import com.components.campusconnectbackend.domain.Role;
import com.components.campusconnectbackend.dto.MenuItemDTO;
import com.components.campusconnectbackend.dto.MenuItemRoleDTO;
import com.components.campusconnectbackend.dto.RoleDTO;
import com.components.campusconnectbackend.repository.MenuItemRepository;
import com.components.campusconnectbackend.repository.MenuItemRoleRepository;
import com.components.campusconnectbackend.repository.PersonRoleRepository;
import com.components.campusconnectbackend.repository.RoleRepository;
import com.components.campusconnectbackend.service.MenuItemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemRoleServiceImpl implements MenuItemRoleService {

    private final MenuItemRoleRepository menuItemRoleRepository;
    private final MenuItemRepository menuItemRepository;
    private final RoleRepository roleRepository;
    private final PersonRoleRepository personRoleRepository;

    @Autowired
    public MenuItemRoleServiceImpl(
            MenuItemRoleRepository menuItemRoleRepository,
            MenuItemRepository menuItemRepository,
            RoleRepository roleRepository,
            PersonRoleRepository personRoleRepository) {
        this.menuItemRoleRepository = menuItemRoleRepository;
        this.menuItemRepository = menuItemRepository;
        this.roleRepository = roleRepository;
        this.personRoleRepository = personRoleRepository;
    }

    @Override
    public MenuItemRoleDTO assignRoleToMenuItem(Integer menuItemId, Integer roleId) {
        // Check if menu item and role exist
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item with ID: " + menuItemId + " not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role with ID: " + roleId + " not found"));

        // Check if the assignment already exists
        if (menuItemRoleRepository.existsByIdMenuItemIdAndIdRoleId(menuItemId, roleId)) {
            throw new RuntimeException("Menu item already has this role assigned");
        }

        // Create new MenuItemRole entity
        MenuItemRole menuItemRole = new MenuItemRole();
        menuItemRole.setId(new MenuItemRole.MenuItemRoleId(menuItemId, roleId));
        menuItemRole.setMenuItem(menuItem);
        menuItemRole.setRole(role);

        // Save and return
        MenuItemRole savedMenuItemRole = menuItemRoleRepository.save(menuItemRole);
        return convertToDTO(savedMenuItemRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemRoleDTO> getAllMenuItemRoles() {
        return menuItemRoleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getRolesForMenuItem(Integer menuItemId) {
        return menuItemRoleRepository.findByIdMenuItemId(menuItemId).stream()
                .map(MenuItemRole::getRole)
                .map(this::convertRoleToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getMenuItemsForRole(Integer roleId) {
        return menuItemRoleRepository.findByIdRoleId(roleId).stream()
                .map(MenuItemRole::getMenuItem)
                .map(this::convertMenuItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(Integer menuItemId, Integer roleId) {
        return menuItemRoleRepository.existsByIdMenuItemIdAndIdRoleId(menuItemId, roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getMenuItemsForUser(String personUid) {
        // Get all roles associated with the person
        List<PersonRole> personRoles = personRoleRepository.findByIdPersonUid(personUid);

        if (personRoles.isEmpty()) {
            return new ArrayList<>();
        }

        // Extract role IDs
        Set<Integer> roleIds = personRoles.stream()
                .map(personRole -> personRole.getId().getRoleId())
                .collect(Collectors.toSet());

        // Get all menu items for these roles
        Set<MenuItem> menuItems = new HashSet<>();
        for (Integer roleId : roleIds) {
            List<MenuItemRole> menuItemRoles = menuItemRoleRepository.findByIdRoleId(roleId);
            menuItemRoles.forEach(menuItemRole -> menuItems.add(menuItemRole.getMenuItem()));
        }

        // Convert to DTOs
        return menuItems.stream()
                .map(this::convertMenuItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeRoleFromMenuItem(Integer menuItemId, Integer roleId) {
        // Check if the assignment exists
        if (!menuItemRoleRepository.existsByIdMenuItemIdAndIdRoleId(menuItemId, roleId)) {
            throw new RuntimeException("Menu item does not have this role assigned");
        }

        menuItemRoleRepository.deleteByIdMenuItemIdAndIdRoleId(menuItemId, roleId);
    }

    @Override
    public void removeAllRolesFromMenuItem(Integer menuItemId) {
        menuItemRoleRepository.deleteByIdMenuItemId(menuItemId);
    }

    @Override
    public void removeMenuItemFromAllRoles(Integer roleId) {
        menuItemRoleRepository.deleteByIdRoleId(roleId);
    }

    // Helper methods to convert between entities and DTOs
    private MenuItemRoleDTO convertToDTO(MenuItemRole menuItemRole) {
        MenuItem menuItem = menuItemRole.getMenuItem();
        Role role = menuItemRole.getRole();

        return new MenuItemRoleDTO(
                menuItem.getId(),
                menuItem.getDescription(),
                menuItem.getPath(),
                role.getId(),
                role.getName(),
                role.getHierarchy()
        );
    }

    private RoleDTO convertRoleToDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getHierarchy()
        );
    }

    private MenuItemDTO convertMenuItemToDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getDescription(),
                menuItem.getPath(),
                menuItem.getIndex()
        );
    }
}