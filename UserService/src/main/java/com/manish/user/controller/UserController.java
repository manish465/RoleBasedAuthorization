package com.manish.user.controller;

import com.manish.common.request.AddUserRequestDTO;
import com.manish.common.request.UpdateUserRequestDTO;
import com.manish.common.response.GeneralMessageResponseDTO;
import com.manish.common.response.GetUserResponseDTO;
import com.manish.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/users/add-user")
    public ResponseEntity<GeneralMessageResponseDTO> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO) {
        log.info("Received request to add user: {}", addUserRequestDTO);
        return new ResponseEntity<>(userService.addUser(addUserRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/users/get-user")
    public ResponseEntity<GetUserResponseDTO> getUser(@RequestParam String userId) {
        log.info("Received request to get user with ID: {}", userId);
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PutMapping("/api/v1/users/update-user")
    public ResponseEntity<GeneralMessageResponseDTO> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        log.info("Received request to update user: {}", updateUserRequestDTO);
        return new ResponseEntity<>(userService.updateUser(updateUserRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/users/delete-user")
    public ResponseEntity<GeneralMessageResponseDTO> deleteUser(@RequestParam String userId) {
        log.info("Received request to delete user with ID: {}", userId);
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/users/delete-all-users")
    public ResponseEntity<GeneralMessageResponseDTO> deleteAllUsers() {
        log.info("Received request to delete all users");
        return new ResponseEntity<>(userService.deleteAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/health-check")
    public ResponseEntity<GeneralMessageResponseDTO> healthCheck() {
        log.info("Received health check request");
        return new ResponseEntity<>(new GeneralMessageResponseDTO("Service is running"), HttpStatus.OK);
    }
}
