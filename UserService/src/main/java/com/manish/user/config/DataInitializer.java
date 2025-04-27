package com.manish.user.config;

import com.manish.user.dao.PermissionDAO;
import com.manish.user.dao.RoleDAO;
import com.manish.user.model.PermissionModel;
import com.manish.user.model.RoleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final RoleDAO roleDAO;
    private final PermissionDAO permissionDAO;
    private Map<String, Set<String>> rolePathMap;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        initializeData();
        initializePermissions();
        initializeRoles();
        log.info("Data initialization completed");
    }

    private void initializeData() {
        rolePathMap = new HashMap<>();
        rolePathMap.put("ADMIN",
                Set.of(
                        "/api/v1/users/update-user",
                        "/api/v1/users/delete-user",
                        "/api/v1/school/get-admin",
                        "/api/v1/school/get-management",
                        "/api/v1/school/get-teacher",
                        "/api/v1/school/get-student",
                        "/api/v1/school/get-guest"
                )
        );

        rolePathMap.put("MANAGMENT",
                Set.of(
                        "/api/v1/users/update-user",
                        "/api/v1/users/delete-user",
                        "/api/v1/school/get-management",
                        "/api/v1/school/get-teacher",
                        "/api/v1/school/get-student",
                        "/api/v1/school/get-guest"
                )
        );

        rolePathMap.put("TEACHER",
                Set.of(
                        "/api/v1/users/update-user",
                        "/api/v1/users/delete-user",
                        "/api/v1/school/get-teacher",
                        "/api/v1/school/get-student",
                        "/api/v1/school/get-guest"
                )
        );

        rolePathMap.put("STUDENT",
                Set.of(
                        "/api/v1/school/get-student"
                )
        );

        rolePathMap.put("GUEST",
                Set.of(
                        "/api/v1/school/get-guest"
                )
        );
    }

    private void initializePermissions() {
        Set<String> allPaths = rolePathMap
                .values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        allPaths.forEach(path -> {
            if (!permissionDAO.existsById(path)) {
                PermissionModel permission = PermissionModel.builder()
                        .uriPath(path)
                        .createdAt(LocalDateTime.now())
                        .build();
                permissionDAO.save(permission);
                log.info("Created permission: {}", path);
            }
        });
    }

    private void initializeRoles() {
        rolePathMap.forEach((roleName, permissions) -> {
            RoleModel role;
            if (!roleDAO.existsById(roleName)) {
                // Create new role
                role = RoleModel.builder()
                        .roleName(roleName)
                        .permissions(new HashSet<>())
                        .createdAt(LocalDateTime.now())
                        .build();
            } else {
                // Get existing role
                role = roleDAO.findById(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                // Clear existing permissions
                role.getPermissions().clear();
            }

            // Get and set permissions
            Set<PermissionModel> permissionModels = permissions.stream()
                    .map(path -> permissionDAO.findById(path)
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + path)))
                    .collect(Collectors.toSet());

            role.setPermissions(permissionModels);

            // Save role
            roleDAO.save(role);
            log.info("{} role: {} with {} permissions",
                    roleDAO.existsById(roleName) ? "Updated" : "Created",
                    roleName,
                    permissions.size());
        });
    }
}
