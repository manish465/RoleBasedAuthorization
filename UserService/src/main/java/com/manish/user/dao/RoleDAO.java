package com.manish.user.dao;

import com.manish.user.model.PermissionModel;
import com.manish.user.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleDAO extends JpaRepository<RoleModel, String> {
    Set<RoleModel> findByRoleNameIn(Set<String> roleNames);
    Optional<RoleModel> findByRoleName(String roleName);
    Set<RoleModel> findByPermissionsContaining(PermissionModel permission);
}
