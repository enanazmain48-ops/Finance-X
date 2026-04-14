package com.finance.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private final String id;
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    public Expense(LocalDate date, double amount, String category, String description) {
        this.id = generateId();
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public Expense(String id, LocalDate date, double amount, String category, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    private static String generateId() {
        return "EXP_" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %.2f | %s | %s", 
            getFormattedDate(), category, amount, description, id);
    }
}