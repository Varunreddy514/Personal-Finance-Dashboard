package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.model.Expense;
import com.example.personalfinancetracker.model.User;
import com.example.personalfinancetracker.repository.ExpenseRepository;
import com.example.personalfinancetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new expense for a user.
     */
    public Expense createExpense(Long userId, Expense expense) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    /**
     * Gets all expenses for a specific user.
     */
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    /**
     * Updates an existing expense.
     * Only allows update if the expense belongs to the given user.
     */
    public Optional<Expense> updateExpense(Long userId, Long expenseId, Expense updatedDetails) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findByIdAndUserId(expenseId, userId);

        if (existingExpenseOpt.isPresent()) {
            Expense existingExpense = existingExpenseOpt.get();
            existingExpense.setAmount(updatedDetails.getAmount());
            existingExpense.setDescription(updatedDetails.getDescription());
            existingExpense.setCategory(updatedDetails.getCategory());
            existingExpense.setDate(updatedDetails.getDate());
            
            return Optional.of(expenseRepository.save(existingExpense));
        }
        return Optional.empty();
    }

    /**
     * Deletes an expense if it belongs to the given user.
     */
    public boolean deleteExpense(Long userId, Long expenseId) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findByIdAndUserId(expenseId, userId);

        if (existingExpenseOpt.isPresent()) {
            expenseRepository.delete(existingExpenseOpt.get());
            return true;
        }
        return false;
    }
}