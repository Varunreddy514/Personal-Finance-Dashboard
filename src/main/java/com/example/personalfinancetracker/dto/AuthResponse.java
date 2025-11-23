package com.example.personalfinancetracker.dto;

// DTO for sending authentication success response back to the client
public class AuthResponse {
    private Long userId;
    private String username;
    private String token; // Represents a JWT or session token for future requests

    public AuthResponse(Long userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getToken() { return token; }
}