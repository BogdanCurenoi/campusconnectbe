package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.MenuItemDTO;
import com.components.campusconnectbackend.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // Create a new menu item
    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemService.createMenuItem(menuItemDTO);
        return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
    }

    // Get all menu items
    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> menuItems = menuItemService.getAllMenuItems();
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Get all menu items ordered by index
    @GetMapping("/ordered")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItemsOrderedByIndex() {
        List<MenuItemDTO> menuItems = menuItemService.getAllMenuItemsOrderedByIndex();
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Get menu item by ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Integer id) {
        return menuItemService.getMenuItemById(id)
                .map(menuItemDTO -> new ResponseEntity<>(menuItemDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get menu item by description
    @GetMapping("/by-description")
    public ResponseEntity<MenuItemDTO> getMenuItemByDescription(@RequestParam String description) {
        return menuItemService.getMenuItemByDescription(description)
                .map(menuItemDTO -> new ResponseEntity<>(menuItemDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get menu item by path
    @GetMapping("/by-path")
    public ResponseEntity<MenuItemDTO> getMenuItemByPath(@RequestParam String path) {
        return menuItemService.getMenuItemByPath(path)
                .map(menuItemDTO -> new ResponseEntity<>(menuItemDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get menu item by index
    @GetMapping("/by-index/{index}")
    public ResponseEntity<MenuItemDTO> getMenuItemByIndex(@PathVariable Integer index) {
        return menuItemService.getMenuItemByIndex(index)
                .map(menuItemDTO -> new ResponseEntity<>(menuItemDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get menu items by index range
    @GetMapping("/index-range")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByIndexRange(
            @RequestParam Integer startIndex,
            @RequestParam Integer endIndex) {
        List<MenuItemDTO> menuItems = menuItemService.getMenuItemsByIndexRange(startIndex, endIndex);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Search menu items by description
    @GetMapping("/search")
    public ResponseEntity<List<MenuItemDTO>> searchMenuItemsByDescription(@RequestParam String description) {
        List<MenuItemDTO> menuItems = menuItemService.searchMenuItemsByDescription(description);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    // Update menu item
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Integer id, @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDTO);
            return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update menu item index only
    @PatchMapping("/{id}/index")
    public ResponseEntity<MenuItemDTO> updateMenuItemIndex(
            @PathVariable Integer id,
            @RequestParam Integer newIndex) {
        try {
            MenuItemDTO updatedMenuItem = menuItemService.updateMenuItemIndex(id, newIndex);
            return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Reorder menu items
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderMenuItems(@RequestBody List<Integer> menuItemIds) {
        try {
            menuItemService.reorderMenuItems(menuItemIds);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete menu item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Integer id) {
        if (menuItemService.existsById(id)) {
            menuItemService.deleteMenuItem(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}