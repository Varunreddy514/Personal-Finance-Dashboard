package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.dto.AuthResponse;
import com.example.personalfinancetracker.dto.LoginRequest;
import com.example.personalfinancetracker.dto.RegisterRequest;
import com.example.personalfinancetracker.model.User;
import com.example.personalfinancetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerNewUser(request);
            // In a real app, generate and return a token here too
            AuthResponse response = new AuthResponse(user.getId(), user.getUsername(), "MOCK_TOKEN_" + user.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.authenticateUser(request);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In a real app, generate a secure token (JWT)
            AuthResponse response = new AuthResponse(user.getId(), user.getUsername(), "MOCK_TOKEN_" + user.getId());
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}