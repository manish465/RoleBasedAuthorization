package com.manish.user.service;

import com.manish.common.request.SignInRequestDTO;
import com.manish.common.response.CreateJWTResponseDTO;
import com.manish.common.response.SignInResponseDTO;
import com.manish.common.response.ValidateUserResponseDTO;
import com.manish.user.dao.UserDAO;
import com.manish.user.exception.ApplicationException;
import com.manish.user.model.RoleModel;
import com.manish.user.model.UserModel;
import com.manish.user.validate.AuthValidate;
import com.manish.user.validate.UserValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthValidate authValidate;
    private final UserValidate userValidate;
    private final UserDAO userDAO;

    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        log.info("Received request to sign in user: {}", signInRequestDTO);

        UserModel userModel = authValidate.validateSignInRequestDTO(signInRequestDTO);



        return SignInResponseDTO.builder()
                .userID(userModel.getUserID())
                .token(JWTService.generateToken(userModel.getUserID(), userModel.getRoles().stream().map(RoleModel::getRoleName).collect(Collectors.toSet())))
                .roles(userModel.getRoles().stream().map(RoleModel::getRoleName).collect(Collectors.toSet()))
                .build();
    }

    public CreateJWTResponseDTO createJWT(String userID) {
        log.info("Received request to create JWT for user with ID: {}", userID);

        Optional<UserModel> optionalUserModel = userDAO.findById(userID);

        userValidate.validateUser(userID, optionalUserModel);

        return CreateJWTResponseDTO.builder()
                .token(JWTService.generateToken(optionalUserModel.get().getUserID(), optionalUserModel.get().getRoles().stream().map(RoleModel::getRoleName).collect(Collectors.toSet())))
                .build();
    }

    public ValidateUserResponseDTO validateUser(String token) {
        log.info("Received request to validate user with token: {}", token);

        if(!JWTService.validateToken(token)) {
            throw new ApplicationException("Invalid token");
        }

        String userID = JWTService.getSubjectFromToken(token);

        Optional<UserModel> optionalUserModel = userDAO.findById(userID);

        userValidate.validateUser(userID, optionalUserModel);

        return ValidateUserResponseDTO.builder()
                .userID(userID)
                .roles(optionalUserModel.get().getRoles().stream().map(RoleModel::getRoleName).collect(Collectors.toSet()))
                .build();
    }
}
