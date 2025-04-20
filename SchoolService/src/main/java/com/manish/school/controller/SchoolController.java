package com.manish.school.controller;

import com.manish.common.response.GeneralMessageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchoolController {
    @GetMapping("/api/v1/school/get-admin")
    public ResponseEntity<GeneralMessageResponseDTO> getAdmin() {
        log.info("Received request to get admin");
        return ResponseEntity.ok(new GeneralMessageResponseDTO("Admin role"));
    }

    @GetMapping("/api/v1/school/get-management")
    public ResponseEntity<GeneralMessageResponseDTO> getManagement() {
        log.info("Received request to get management");
        return ResponseEntity.ok(new GeneralMessageResponseDTO("Management role"));
    }

    @GetMapping("/api/v1/school/get-teacher")
    public ResponseEntity<GeneralMessageResponseDTO> getTeacher() {
        log.info("Received request to get teacher");
        return ResponseEntity.ok(new GeneralMessageResponseDTO("Teacher role"));
    }

    @GetMapping("/api/v1/school/get-student")
    public ResponseEntity<GeneralMessageResponseDTO> getStudent() {
        log.info("Received request to get student");
        return ResponseEntity.ok(new GeneralMessageResponseDTO("Student role"));
    }

    @GetMapping("/api/v1/school/get-guest")
    public ResponseEntity<GeneralMessageResponseDTO> getGuest() {
        log.info("Received request to get guest");
        return ResponseEntity.ok(new GeneralMessageResponseDTO("Guest role"));
    }
}
