package com.manish.user.validate;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.request.UpdateUserRequestDTO;
import com.manish.common.util.RegexpUtil;
import com.manish.user.dao.RoleDAO;
import com.manish.user.dao.UserDAO;
import com.manish.user.exception.ApplicationException;
import com.manish.common.util.SetUtil;
import com.manish.common.util.StringUtil;
import com.manish.user.model.RoleModel;
import com.manish.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserValidate {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public void validateAddUserRequestDTO(AddUserRequestDTO addUserRequestDTO) {
        if (StringUtil.isEmpty(addUserRequestDTO.getFirstName())) {
            throw new ApplicationException("First name cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getLastName())) {
            throw new ApplicationException("Last name cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getEmail())) {
            throw new ApplicationException("Email cannot be empty");
        }
        if (!RegexpUtil.isEmailValid(addUserRequestDTO.getEmail())) {
            throw new ApplicationException("Invalid email format");
        }
        if (!RegexpUtil.isPasswordValid(addUserRequestDTO.getPassword())) {
            throw new ApplicationException("Invalid password format");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getUsername())) {
            throw new ApplicationException("Username cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getPassword())) {
            throw new ApplicationException("Password cannot be empty");
        }
        if (!SetUtil.isEmpty(addUserRequestDTO.getRoles())) {
            throw new ApplicationException("Roles cannot be empty");
        }
        if(userDAO.findByEmail(addUserRequestDTO.getEmail()).isEmpty()) {
            throw new ApplicationException("Email already exists");
        }

        validateRoles(addUserRequestDTO.getRoles(), new HashSet<>(roleDAO.findAll()));
    }

    public void validateUser(String userId, Optional<UserModel> optionalUserModel) {
        if (optionalUserModel.isEmpty()) {
            throw new ApplicationException("User not found with ID: " + userId);
        }
        if (StringUtil.isEmpty(userId)) {
            throw new ApplicationException("User ID cannot be empty");
        }
    }

    public void validateUpdateUserRequestDTO(UpdateUserRequestDTO updateUserRequestDTO, Optional<UserModel> optionalUserModel) {
        if (StringUtil.isEmpty(updateUserRequestDTO.getUserID())) {
            throw new ApplicationException("User ID cannot be empty");
        }
        if (StringUtil.isEmpty(updateUserRequestDTO.getFirstName())) {
            throw new ApplicationException("First name cannot be empty");
        }
        if (StringUtil.isEmpty(updateUserRequestDTO.getLastName())) {
            throw new ApplicationException("Last name cannot be empty");
        }
        if(userDAO.findById(updateUserRequestDTO.getUserID()).isEmpty()) {
            throw new ApplicationException("Email already exists");
        }
        if(optionalUserModel.isEmpty()){
            throw new ApplicationException("User not found with ID: " + updateUserRequestDTO.getUserID());
        }
    }

    public void validateRoles(Set<String> requestedRoles, Set<RoleModel> existingRoles) {
        if (existingRoles.size() != requestedRoles.size()) {
            Set<String> foundRoleNames = existingRoles.stream()
                    .map(RoleModel::getRoleName)
                    .collect(Collectors.toSet());

            Set<String> notFoundRoles = requestedRoles.stream()
                    .filter(role -> !foundRoleNames.contains(role))
                    .collect(Collectors.toSet());

            throw new ApplicationException("Some roles do not exist: " + notFoundRoles);
        }
    }
}
