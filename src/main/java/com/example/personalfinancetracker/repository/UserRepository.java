package com.example.personalfinancetracker.repository;

import com.example.personalfinancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find a user by their email for login
    Optional<User> findByEmail(String email);
}