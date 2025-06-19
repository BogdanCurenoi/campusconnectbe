package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    List<Role> findByHierarchy(Integer hierarchy);
    List<Role> findByHierarchyLessThanEqual(Integer hierarchy);
    List<Role> findByHierarchyGreaterThanEqual(Integer hierarchy);
    List<Role> findAllByOrderByHierarchyAsc();
    List<Role> findAllByOrderByHierarchyDesc();
}