package com.project.monolith_e_commerce.web.dto.auth;

import com.project.monolith_e_commerce.domain.user.Role;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String name,
        String email,
        Role role,
        Instant createdAt
) {}

