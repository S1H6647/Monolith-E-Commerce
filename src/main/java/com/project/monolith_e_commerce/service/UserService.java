package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.user.User;
import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.UserRepository;
import com.project.monolith_e_commerce.security.jwt.JwtUtil;
import com.project.monolith_e_commerce.web.dto.auth.LoginRequest;
import com.project.monolith_e_commerce.web.dto.auth.LoginResponse;
import com.project.monolith_e_commerce.web.dto.auth.RegisterRequest;
import com.project.monolith_e_commerce.web.dto.auth.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user account.
     * Throws {@link IllegalArgumentException} if the email is already taken.
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
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

    /**
     * Authenticate a user and return a signed JWT token.
     * Throws {@link org.springframework.security.authentication.BadCredentialsException}
     * if credentials are invalid.
     */
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new LoginResponse(jwtUtil.generateToken(user));
    }
}
