package com.project.monolith_e_commerce.web.dto.auth;

import com.project.monolith_e_commerce.domain.user.Role;
import com.project.monolith_e_commerce.domain.user.User;

public record LoginResponse(
        String name,
        String email,
        Role role,
        String token
) {
    public static LoginResponse from(User user, String token) {
        return new LoginResponse(
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}

