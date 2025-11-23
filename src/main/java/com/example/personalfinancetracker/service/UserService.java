package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.dto.LoginRequest;
import com.example.personalfinancetracker.dto.RegisterRequest;
import com.example.personalfinancetracker.model.User;
import com.example.personalfinancetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Note: In a real app, you would use Spring Security's PasswordEncoder
    // For this example, we'll use a placeholder for hashing/checking
    // Assuming you have a PasswordEncoder bean configured:
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     * @return The newly created User object.
     * @throws RuntimeException if user already exists.
     */
    public User registerNewUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        // Use a proper hashing mechanism here (e.g., BCryptPasswordEncoder)
        // For simplicity, we'll store a mock hash:
        user.setPasswordHash("MOCK_HASH_" + request.getPassword()); // In production, use passwordEncoder.encode(request.getPassword());

        return userRepository.save(user);
    }

    /**
     * Authenticates a user.
     * @return Optional<User> if login is successful.
     */
    public Optional<User> authenticateUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // In production, compare hashed passwords:
            // boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
            boolean passwordMatches = ("MOCK_HASH_" + request.getPassword()).equals(user.getPasswordHash());

            if (passwordMatches) {
                return userOpt;
            }
        }
        return Optional.empty();
    }
}