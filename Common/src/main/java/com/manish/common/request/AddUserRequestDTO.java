package com.manish.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddUserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String bio;
    private Set<String> roles;
}
