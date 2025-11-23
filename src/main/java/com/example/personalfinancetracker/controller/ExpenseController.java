package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.model.Expense;
import com.example.personalfinancetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // --- Placeholder for fetching authenticated User ID ---
    // In a real Spring Security app, you would get the user ID from the SecurityContext,
    // which is derived from the JWT/Session token sent in the request header.
    // For this example, we'll simulate by assuming the user ID is in a request header/path variable
    // or by hardcoding a mock ID, or requiring it in the path (e.g., /api/expenses/{userId} )

    private Long getAuthenticatedUserId(String token) {
        // Simple mock extraction: assuming the token is "MOCK_TOKEN_1", we extract 1
        try {
            return Long.parseLong(token.split("_")[2]);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed or token invalid.");
        }
    }

    // GET /api/expenses -> Get all expenses for the authenticated user
    @GetMapping
    public ResponseEntity<List<Expense>> getAllUserExpenses(@RequestHeader("Authorization") String token) {
        try {
            Long userId = getAuthenticatedUserId(token);
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            return ResponseEntity.ok(expenses);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // POST /api/expenses -> Create a new expense
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestHeader("Authorization") String token, @RequestBody Expense expense) {
        try {
            Long userId = getAuthenticatedUserId(token);
            Expense createdExpense = expenseService.createExpense(userId, expense);
            return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // PUT /api/expenses/{id} -> Update an existing expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Expense expenseDetails) {
        try {
            Long userId = getAuthenticatedUserId(token);
            Optional<Expense> updatedExpense = expenseService.updateExpense(userId, id, expenseDetails);

            return updatedExpense
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // DELETE /api/expenses/{id} -> Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            Long userId = getAuthenticatedUserId(token);
            boolean deleted = expenseService.deleteExpense(userId, id);
            return deleted ? ResponseEntity.noContent().build() : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}