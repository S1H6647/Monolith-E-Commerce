package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.service.UserService;
import com.project.monolith_e_commerce.web.dto.auth.LoginRequest;
import com.project.monolith_e_commerce.web.dto.auth.LoginResponse;
import com.project.monolith_e_commerce.web.dto.auth.RegisterRequest;
import com.project.monolith_e_commerce.web.dto.auth.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user authentication operations.
 *
 * <ul>
 *   <li>POST /api/auth/register — register a new user account</li>
 *   <li>POST /api/auth/login   — authenticate and receive a JWT token</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /** Register a new user account. Returns the created user without a token. */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    /** Authenticate and receive a JWT bearer token. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
