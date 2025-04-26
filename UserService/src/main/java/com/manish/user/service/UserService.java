package com.manish.user.service;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.request.UpdateUserRequestDTO;
import com.manish.common.response.GeneralMessageResponseDTO;
import com.manish.common.response.GetUserResponseDTO;
import com.manish.user.dao.RoleDAO;
import com.manish.user.dao.UserDAO;
import com.manish.user.mapper.UserMapper;
import com.manish.user.model.RoleModel;
import com.manish.user.model.UserModel;
import com.manish.user.validate.UserValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final UserMapper userMapper;
    private final UserValidate userValidate;

    public GeneralMessageResponseDTO addUser(AddUserRequestDTO addUserRequestDTO) {
        log.info("Received request to add user: {}", addUserRequestDTO);

        Set<RoleModel> existingRoles = roleDAO.findByRoleNameIn(addUserRequestDTO.getRoles());
        Set<RoleModel> allRoles = new HashSet<>(roleDAO.findAll());
        Optional<UserModel> optionalUserModel = userDAO.findByEmail(addUserRequestDTO.getEmail());

        userValidate.validateAddUserRequestDTO(addUserRequestDTO, optionalUserModel, allRoles);
        UserModel savedUser = userDAO.save(userMapper.toEntity(addUserRequestDTO, existingRoles));

        return new GeneralMessageResponseDTO("User added successfully with userID : " + savedUser.getUserID());
    }

    public GetUserResponseDTO getUser(String userId) {
        log.info("Received request to get user with ID: {}", userId);

        Optional<UserModel> exitingUserModelOptional = userDAO.findById(userId);
        userValidate.validateUser(userId, exitingUserModelOptional);

        return userMapper.toDTO(exitingUserModelOptional.get());
    }

    public GeneralMessageResponseDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        log.info("Received request to update user: {}", updateUserRequestDTO);

        Optional<UserModel> exitingUserModelOptional = userDAO.findById(updateUserRequestDTO.getUserID());
        userValidate.validateUpdateUserRequestDTO(updateUserRequestDTO, exitingUserModelOptional);

        UserModel savedUser = userDAO.save(userMapper.toEntity(updateUserRequestDTO, exitingUserModelOptional.get()));

        return new GeneralMessageResponseDTO("User updated successfully with userID : " + savedUser.getUserID());
    }

    public GeneralMessageResponseDTO deleteUser(String userId) {
        log.info("Received request to delete user with ID: {}", userId);

        Optional<UserModel> exitingUserModelOptional = userDAO.findById(userId);
        userValidate.validateUser(userId, exitingUserModelOptional);

        userDAO.delete(exitingUserModelOptional.get());

        return new GeneralMessageResponseDTO("User deleted successfully with userID : " + userId);
    }

    public GeneralMessageResponseDTO deleteAllUsers() {
        log.info("Received request to delete all users");

        userDAO.deleteAll();

        return new GeneralMessageResponseDTO("All users deleted successfully");
    }
}
