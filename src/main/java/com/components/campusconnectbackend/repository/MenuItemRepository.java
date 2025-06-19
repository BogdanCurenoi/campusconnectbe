package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    Optional<MenuItem> findByDescription(String description);

    Optional<MenuItem> findByPath(String path);

    List<MenuItem> findByDescriptionContaining(String description);

    // New methods for index-based operations
    List<MenuItem> findAllByOrderByIndexAsc();

    List<MenuItem> findByIndexBetweenOrderByIndexAsc(Integer startIndex, Integer endIndex);

    Optional<MenuItem> findByIndex(Integer index);
}