package com.manish.user.controller;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.response.CreateJWTResponseDTO;
import com.manish.common.response.GeneralMessageResponseDTO;
import com.manish.common.request.SignInRequestDTO;
import com.manish.common.response.SignInResponseDTO;
import com.manish.common.response.ValidateUserResponseDTO;
import com.manish.user.service.AuthService;
import com.manish.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/api/v1/auth/sign-up")
    public ResponseEntity<GeneralMessageResponseDTO> signUp(@RequestBody AddUserRequestDTO addUserRequestDTO) {
        log.info("Received request to sign up user: {}", addUserRequestDTO);
        return new ResponseEntity<>(userService.addUser(addUserRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/auth/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO){
        log.info("Received request to sign in user: {}", signInRequestDTO);
        return new ResponseEntity<>(authService.signIn(signInRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/create-jwt")
    public ResponseEntity<CreateJWTResponseDTO> createJWT(@RequestParam String userID){
        log.info("Received request to create JWT for user with ID: {}", userID);
        return new ResponseEntity<>(authService.createJWT(userID), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/validate-user")
    public ResponseEntity<ValidateUserResponseDTO> validateUser(@RequestParam String token){
        log.info("Received request to validate user with token: {}", token);
        return new ResponseEntity<>(authService.validateUser(token), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/health-check")
    public ResponseEntity<GeneralMessageResponseDTO> healthCheck(){
        log.info("Received health check request");
        return new ResponseEntity<>(new GeneralMessageResponseDTO("Service is running"), HttpStatus.OK);
    }
}
