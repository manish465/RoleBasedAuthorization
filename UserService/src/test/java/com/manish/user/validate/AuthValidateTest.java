package com.manish.user.validate;

import com.manish.common.request.SignInRequestDTO;
import com.manish.user.exception.ApplicationException;
import com.manish.user.model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthValidateTest {
    private AuthValidate authValidate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authValidate = new AuthValidate();
    }

    @Test
    void validateSignInRequestDTO_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = "hashedPassword";

        SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        UserModel userModel = UserModel.builder()
                .email(email)
                .hashPassword(hashedPassword)
                .build();

        // Arrange Mock
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> authValidate.validateSignInRequestDTO(
                signInRequestDTO, passwordEncoder, Optional.of(userModel)
        ));
    }

    @Test
    void validateSignInRequestDTO_EmptyEmail() {
        // Arrange
        String email = "";
        String password = "password123";

        SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        // Act & Assert
        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authValidate.validateSignInRequestDTO(
                        signInRequestDTO,
                        passwordEncoder,
                        Optional.empty()
                )
        );
        assertEquals("User not found with email: " + email, exception.getMessage());
    }
}
