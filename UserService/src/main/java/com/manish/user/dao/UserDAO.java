package com.manish.user.dao;

import com.manish.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
}
