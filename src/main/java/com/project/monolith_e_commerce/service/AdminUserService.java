package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.user.Role;
import com.project.monolith_e_commerce.domain.user.User;
import com.project.monolith_e_commerce.repository.UserRepository;
import com.project.monolith_e_commerce.web.dto.auth.RegisterRequest;
import com.project.monolith_e_commerce.web.dto.auth.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STAFF)
                .build();

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole(),
                saved.getCreatedAt()
        );
    }
}
