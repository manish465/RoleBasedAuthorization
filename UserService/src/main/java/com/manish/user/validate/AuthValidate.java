package com.manish.user.validate;

import com.manish.common.request.SignInRequestDTO;
import com.manish.common.util.StringUtil;
import com.manish.user.dao.UserDAO;
import com.manish.user.exception.ApplicationException;
import com.manish.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthValidate {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserModel validateSignInRequestDTO(SignInRequestDTO signInRequestDTO) {
        Optional<UserModel> userModelOptional = userDAO.findByEmail(signInRequestDTO.getEmail());

        if(!StringUtil.isEmpty(signInRequestDTO.getEmail())) {
            throw new ApplicationException("Email cannot be empty");
        }
        if (userModelOptional.isEmpty()) {
            throw new ApplicationException("User not found with email: " + signInRequestDTO.getEmail());
        }
        if(!StringUtil.isEmpty(signInRequestDTO.getPassword())) {
            throw new ApplicationException("Password cannot be empty");
        }
        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), userModelOptional.get().getHashPassword())) {
            throw new ApplicationException("Invalid password");
        }

        return userModelOptional.get();
    }
}
