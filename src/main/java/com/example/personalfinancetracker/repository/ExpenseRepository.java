package com.example.personalfinancetracker.repository;

import com.example.personalfinancetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Custom query to find all expenses belonging to a specific user
    List<Expense> findByUserId(Long userId);

    // Custom query to find a specific expense by ID and belonging to a specific user
    Optional<Expense> findByIdAndUserId(Long expenseId, Long userId);
}