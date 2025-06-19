package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.MenuItem;
import com.components.campusconnectbackend.dto.MenuItemDTO;
import com.components.campusconnectbackend.repository.MenuItemRepository;
import com.components.campusconnectbackend.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = convertToEntity(menuItemDTO);

        // If no index is provided, set it to the next available index
        if (menuItem.getIndex() == null) {
            List<MenuItem> allItems = menuItemRepository.findAllByOrderByIndexAsc();
            int nextIndex = allItems.isEmpty() ? 1 : allItems.get(allItems.size() - 1).getIndex() + 1;
            menuItem.setIndex(nextIndex);
        }

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return convertToDTO(savedMenuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAllMenuItemsOrderedByIndex() {
        return menuItemRepository.findAllByOrderByIndexAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> getMenuItemById(Integer id) {
        return menuItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> getMenuItemByDescription(String description) {
        return menuItemRepository.findByDescription(description)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> getMenuItemByPath(String path) {
        return menuItemRepository.findByPath(path)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> getMenuItemByIndex(Integer index) {
        return menuItemRepository.findByIndex(index)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> searchMenuItemsByDescription(String description) {
        return menuItemRepository.findByDescriptionContaining(description).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getMenuItemsByIndexRange(Integer startIndex, Integer endIndex) {
        return menuItemRepository.findByIndexBetweenOrderByIndexAsc(startIndex, endIndex).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDTO updateMenuItem(Integer id, MenuItemDTO menuItemDTO) {
        if (menuItemRepository.existsById(id)) {
            MenuItem menuItem = convertToEntity(menuItemDTO);
            menuItem.setId(id); // Ensure the correct ID is set
            MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
            return convertToDTO(updatedMenuItem);
        } else {
            throw new RuntimeException("Menu item with ID: " + id + " not found");
        }
    }

    @Override
    public MenuItemDTO updateMenuItemIndex(Integer id, Integer newIndex) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item with ID: " + id + " not found"));

        menuItem.setIndex(newIndex);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return convertToDTO(updatedMenuItem);
    }

    @Override
    public void reorderMenuItems(List<Integer> menuItemIds) {
        for (int i = 0; i < menuItemIds.size(); i++) {
            Integer menuItemId = menuItemIds.get(i);
            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new RuntimeException("Menu item with ID: " + menuItemId + " not found"));

            menuItem.setIndex(i + 1); // Set index starting from 1
            menuItemRepository.save(menuItem);
        }
    }

    @Override
    public void deleteMenuItem(Integer id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return menuItemRepository.existsById(id);
    }

    // Helper methods to convert between entity and DTO
    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getDescription(),
                menuItem.getPath(),
                menuItem.getIndex()
        );
    }

    private MenuItem convertToEntity(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = new MenuItem();
        // When creating a new menu item, the ID might be null (auto-incremented)
        if (menuItemDTO.getId() != null) {
            menuItem.setId(menuItemDTO.getId());
        }
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPath(menuItemDTO.getPath());
        menuItem.setIndex(menuItemDTO.getIndex());
        return menuItem;
    }
}