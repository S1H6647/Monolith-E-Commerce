package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.service.AdminUserService;
import com.project.monolith_e_commerce.web.dto.auth.RegisterRequest;
import com.project.monolith_e_commerce.web.dto.auth.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
public class StaffController {

    private final AdminUserService adminUserService;

    /**
     * Register a new staff account.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.register(request));
    }

}
