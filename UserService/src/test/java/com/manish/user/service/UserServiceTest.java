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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidate userValidate;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_shouldAddUserSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String role = "USER";

        AddUserRequestDTO addUserRequestDTO = AddUserRequestDTO.builder()
                .email(email)
                .roles(Set.of(role))
                .build();

        Set<RoleModel> existingRoles = new HashSet<>();
        existingRoles.add(new RoleModel(role));

        UserModel userModel = new UserModel();
        userModel.setUserID("123");

        // Arrange mocks
        when(roleDAO.findByRoleNameIn(anySet())).thenReturn(existingRoles);
        when(roleDAO.findAll()).thenReturn(List.of(new RoleModel(role)));
        when(userDAO.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(AddUserRequestDTO.class), anySet())).thenReturn(userModel);
        doNothing().when(userValidate).validateAddUserRequestDTO(any(), any(), any());
        when(userDAO.save(any(UserModel.class))).thenReturn(userModel);

        // Act
        GeneralMessageResponseDTO response = userService.addUser(addUserRequestDTO);

        // Assert
        assertThat(response.getMessage()).isEqualTo("User added successfully with userID : 123");
        verify(userValidate, times(1)).validateAddUserRequestDTO(any(), any(), any());
        verify(userDAO).save(any(UserModel.class));
        verifyNoMoreInteractions(userDAO, roleDAO, userMapper, userValidate);
    }

    @Test
    void getUser_shouldReturnSingleUserSuccessfully(){
        // Arrange
        String userId = "123";

        UserModel userModel = UserModel.builder()
                .userID(userId)
                .build();

        GetUserResponseDTO responseDTO = GetUserResponseDTO.builder()
                .userID(userId)
                .build();

        // Arrange mocks
        when(userDAO.findById(userId)).thenReturn(Optional.of(userModel));
        doNothing().when(userValidate).validateUser(any(), any());
        when(userMapper.toDTO(any(UserModel.class))).thenReturn(responseDTO);

        // Act
        GetUserResponseDTO response = userService.getUser(userId);

        // Assert
        assertThat(response.getUserID()).isEqualTo(userId);
        verify(userDAO).findById(userId);
        verify(userValidate, times(1)).validateUser(any(), any());
        verifyNoMoreInteractions(userDAO, roleDAO, userMapper, userValidate);
    }

    @Test
    void updateUser_shouldUpdateSuccessfully(){
        // Arrange
        String userID = "123";

        UpdateUserRequestDTO updateUserRequestDTO = UpdateUserRequestDTO.builder()
                .userID(userID)
                .build();

        UserModel exitingUserModel = UserModel.builder()
                .userID(userID)
                .build();

        UserModel savedUser = UserModel.builder()
                .userID(userID)
                .build();

        // Arrange mocks
        when(userDAO.findById(userID)).thenReturn(Optional.of(exitingUserModel));
        doNothing().when(userValidate).validateUpdateUserRequestDTO(any(), any());
        when(userMapper.toEntity(any(UpdateUserRequestDTO.class), any(UserModel.class))).thenReturn(savedUser);
        when(userDAO.save(any(UserModel.class))).thenReturn(savedUser);

        // Act
        GeneralMessageResponseDTO response = userService.updateUser(updateUserRequestDTO);

        // Assert
        assertThat(response.getMessage()).isEqualTo("User updated successfully with userID : " + userID);
        verify(userValidate, times(1)).validateUpdateUserRequestDTO(any(), any());
        verifyNoMoreInteractions(userDAO, roleDAO, userMapper, userValidate);
    }

    @Test
    void deleteUser_shouldDeleteUserSuccessfully(){
        // Arrange
        String userID = "123";

        UserModel exitingUserModel = UserModel.builder()
                .userID(userID)
                .build();

        // Arrange mocks
        when(userDAO.findById(anyString())).thenReturn(Optional.of(exitingUserModel));
        doNothing().when(userValidate).validateUser(eq(userID), any());

        // Act
        GeneralMessageResponseDTO response = userService.deleteUser(userID);

        // Assert
        assertThat(response.getMessage()).isEqualTo("User deleted successfully with userID : " + userID);
        verify(userValidate, times(1)).validateUser(any(), any());
        verify(userDAO, times(1)).deleteById(eq(userID));
        verifyNoMoreInteractions(userDAO, roleDAO, userMapper, userValidate);
    }

    @Test
    void deleteAllUsers_shouldDeleteAllUserSuccessfully(){
        // Act
        GeneralMessageResponseDTO response = userService.deleteAllUsers();

        // Assert
        assertThat(response.getMessage()).isEqualTo("All users deleted successfully");
        verify(userDAO, times(1)).deleteAll();
        verifyNoMoreInteractions(userDAO, roleDAO, userMapper, userValidate);
    }
}
