package com.finance.model;

/**
 * Budget model representing budget constraints for categories
 */
public class Budget {
    private final String id;
    private String category;
    private double amount;
    private String period; // MONTHLY, WEEKLY
    private long createdDate;

    public Budget(String category, double amount, String period) {
        this.id = generateId();
        this.category = category;
        this.amount = amount;
        this.period = period;
        this.createdDate = System.currentTimeMillis();
    }

    public Budget(String id, String category, double amount, String period, long createdDate) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.period = period;
        this.createdDate = createdDate;
    }

    private static String generateId() {
        return "BUD_" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f (%s)", category, amount, period);
    }
}