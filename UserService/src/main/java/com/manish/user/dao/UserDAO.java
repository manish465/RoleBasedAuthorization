package com.manish.user.dao;

import com.manish.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
}
