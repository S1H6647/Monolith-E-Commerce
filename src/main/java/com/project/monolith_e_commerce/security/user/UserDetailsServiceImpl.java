package com.project.monolith_e_commerce.security.user;

import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with " + username + " not found"));

        return UserPrincipal.from(user);
    }
}
