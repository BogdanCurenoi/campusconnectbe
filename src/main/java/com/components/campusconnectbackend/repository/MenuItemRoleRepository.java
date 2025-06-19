package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.MenuItemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRoleRepository extends JpaRepository<MenuItemRole, MenuItemRole.MenuItemRoleId> {

    List<MenuItemRole> findByIdMenuItemId(Integer menuItemId);

    List<MenuItemRole> findByIdRoleId(Integer roleId);

    Optional<MenuItemRole> findByIdMenuItemIdAndIdRoleId(Integer menuItemId, Integer roleId);

    void deleteByIdMenuItemId(Integer menuItemId);

    void deleteByIdRoleId(Integer roleId);

    void deleteByIdMenuItemIdAndIdRoleId(Integer menuItemId, Integer roleId);

    boolean existsByIdMenuItemIdAndIdRoleId(Integer menuItemId, Integer roleId);
}