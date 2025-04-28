package com.manish.user.validate;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.request.UpdateUserRequestDTO;
import com.manish.common.util.RegexpUtil;
import com.manish.user.exception.ApplicationException;
import com.manish.common.util.StringUtil;
import com.manish.user.model.RoleModel;
import com.manish.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserValidate {
    public void validateAddUserRequestDTO(AddUserRequestDTO addUserRequestDTO, Optional<UserModel> optionalUserModel, Set<RoleModel> roleModels) {
        if (StringUtil.isEmpty(addUserRequestDTO.getFirstName())) {
            throw new ApplicationException("First name cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getLastName())) {
            throw new ApplicationException("Last name cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getEmail())) {
            throw new ApplicationException("Email cannot be empty");
        }
        if (StringUtil.isEmpty(addUserRequestDTO.getUsername())) {
            throw new ApplicationException("Username cannot be empty");
        }
        if (!RegexpUtil.isEmailValid(addUserRequestDTO.getEmail())) {
            throw new ApplicationException("Invalid email format");
        }
        if (!RegexpUtil.isPasswordValid(addUserRequestDTO.getPassword())) {
            throw new ApplicationException("Invalid password format");
        }
        if(optionalUserModel.isPresent()) {
            throw new ApplicationException("Email already exists");
        }

        validateRoles(addUserRequestDTO.getRoles(), roleModels);
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
        if(optionalUserModel.isEmpty()){
            throw new ApplicationException("User not found with ID: " + updateUserRequestDTO.getUserID());
        }
    }

    public void validateRoles(Set<String> requestedRoles, Set<RoleModel> existingRoles) {
        Set<String> foundRoleNames = existingRoles.stream()
                .map(RoleModel::getRoleName)
                .collect(Collectors.toSet());

        Set<String> notFoundRoles = requestedRoles.stream()
                .filter(role -> !foundRoleNames.contains(role))
                .collect(Collectors.toSet());

        if(!notFoundRoles.isEmpty()) {
            throw new ApplicationException("Some roles do not exist: " + notFoundRoles);
        }
    }
}
