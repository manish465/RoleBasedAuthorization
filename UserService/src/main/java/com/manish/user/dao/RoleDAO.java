package com.manish.user.dao;

import com.manish.user.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleDAO extends JpaRepository<RoleModel, String> {
    Set<RoleModel> findByRoleNameIn(Set<String> roleNames);
}
