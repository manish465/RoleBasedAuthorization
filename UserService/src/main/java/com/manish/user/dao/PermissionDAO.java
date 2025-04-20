package com.manish.user.dao;

import com.manish.user.model.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDAO extends JpaRepository<PermissionModel, String> {
}
