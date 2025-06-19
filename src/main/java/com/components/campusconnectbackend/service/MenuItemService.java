package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.MenuItemDTO;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {

    // Create
    MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO);

    // Read
    List<MenuItemDTO> getAllMenuItems();
    List<MenuItemDTO> getAllMenuItemsOrderedByIndex();
    Optional<MenuItemDTO> getMenuItemById(Integer id);
    Optional<MenuItemDTO> getMenuItemByDescription(String description);
    Optional<MenuItemDTO> getMenuItemByPath(String path);
    Optional<MenuItemDTO> getMenuItemByIndex(Integer index);
    List<MenuItemDTO> searchMenuItemsByDescription(String description);
    List<MenuItemDTO> getMenuItemsByIndexRange(Integer startIndex, Integer endIndex);

    // Update
    MenuItemDTO updateMenuItem(Integer id, MenuItemDTO menuItemDTO);
    MenuItemDTO updateMenuItemIndex(Integer id, Integer newIndex);

    // Delete
    void deleteMenuItem(Integer id);

    // Reorder operations
    void reorderMenuItems(List<Integer> menuItemIds);

    // Check if exists
    boolean existsById(Integer id);
}