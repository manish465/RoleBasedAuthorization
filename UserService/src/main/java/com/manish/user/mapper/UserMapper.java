package com.manish.user.mapper;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.request.UpdateUserRequestDTO;
import com.manish.common.response.GetUserResponseDTO;
import com.manish.user.model.RoleModel;
import com.manish.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserModel toEntity(AddUserRequestDTO dto, Set<RoleModel> roles) {
        return UserModel.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .hashPassword(passwordEncoder.encode(dto.getPassword()))
                .bio(dto.getBio())
                .roles(roles)
                .build();
    }

    public UserModel toEntity(UpdateUserRequestDTO dto, UserModel userModel) {
        userModel.setFirstName(dto.getFirstName());
        userModel.setLastName(dto.getLastName());
        userModel.setBio(dto.getBio());
        return userModel;
    }

    public GetUserResponseDTO toDTO(UserModel entity) {
        return GetUserResponseDTO.builder()
                .userID(entity.getUserID())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .bio(entity.getBio())
                .roles(entity.getRoles().stream()
                        .map(RoleModel::getRoleName)
                        .collect(Collectors.toSet()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

