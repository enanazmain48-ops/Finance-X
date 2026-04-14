package com.finance.util;

import com.finance.model.Budget;
import com.finance.model.Expense;
import com.finance.model.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    private static final String DATA_DIR = "finance_data";
    private static final String USERS_FILE = "users.csv";
    private static final String EXPENSES_DIR = "expenses";
    private static final String BUDGETS_DIR = "budgets";

    static {
        initializeDirectories();
    }

    // Initialize required directories
    private static void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Files.createDirectories(Paths.get(DATA_DIR, EXPENSES_DIR));
            Files.createDirectories(Paths.get(DATA_DIR, BUDGETS_DIR));
        } catch (IOException e) {
            // Ignore directory initialization failures here; individual operations will fail gracefully.
        }
    }

    // Save a new user to the users CSV file
    public static boolean saveUser(User user) {
        try {
            Path usersPath = Paths.get(DATA_DIR, USERS_FILE);

            if (!Files.exists(usersPath)) {
                Files.createFile(usersPath);
                Files.write(usersPath, "username,password,email,createdDate\n".getBytes());
            }

            String line = String.format("%s,%s,%s,%d%n",
                    user.getUsername(),
                    encryptPassword(user.getPassword()),
                    user.getEmail(),
                    user.getCreatedDate());

            Files.write(usersPath, line.getBytes(),
                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Retrieve a user by username
    public static User getUser(String username) {
        try {
            Path usersPath = Paths.get(DATA_DIR, USERS_FILE);
            if (!Files.exists(usersPath)) {
                return null;
            }

            List<String> lines = Files.readAllLines(usersPath);
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 4 && parts[0].equals(username)) {
                    return new User(
                            parts[0],
                            decryptPassword(parts[1]),
                            parts[2],
                            Long.parseLong(parts[3]));
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    // Check if user exists
    public static boolean userExists(String username) {
        return getUser(username) != null;
    }

    // Save expense for a user
    public static boolean saveExpense(String username, Expense expense) {
        try {
            Path expenseDir = Paths.get(DATA_DIR, EXPENSES_DIR);
            Path userExpenseFile = expenseDir.resolve(username + ".csv");

            if (!Files.exists(userExpenseFile)) {
                Files.createFile(userExpenseFile);
                Files.write(userExpenseFile, "id,date,amount,category,description\n".getBytes());
            }

            String line = String.format("%s,%s,%.2f,%s,%s%n",
                    expense.getId(),
                    expense.getFormattedDate(),
                    expense.getAmount(),
                    expense.getCategory(),
                    sanitizeCSVField(expense.getDescription()));

            Files.write(userExpenseFile, line.getBytes(),
                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Get all expenses for a user
    public static List<Expense> getExpenses(String username) {
        List<Expense> expenses = new ArrayList<>();
        try {
            Path userExpenseFile = Paths.get(DATA_DIR, EXPENSES_DIR, username + ".csv");
            if (!Files.exists(userExpenseFile)) {
                return expenses;
            }

            List<String> lines = Files.readAllLines(userExpenseFile);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 1; i < lines.size(); i++) {
                try {
                    String[] parts = parseCSVLine(lines.get(i));
                    if (parts.length >= 5) {
                        Expense expense = new Expense(
                                parts[0],
                                LocalDate.parse(parts[1], formatter),
                                Double.parseDouble(parts[2]),
                                parts[3],
                                parts[4]);
                        expenses.add(expense);
                    }
                } catch (Exception e) {
                    // Skip malformed rows instead of interrupting the user's full expense list.
                }
            }
        } catch (Exception e) {
            return expenses;
        }
        return expenses;
    }

    // Delete an expense
    public static boolean deleteExpense(String username, String expenseId) {
        try {
            Path userExpenseFile = Paths.get(DATA_DIR, EXPENSES_DIR, username + ".csv");
            if (!Files.exists(userExpenseFile)) {
                return false;
            }

            List<String> lines = Files.readAllLines(userExpenseFile);
            List<String> updatedLines = new ArrayList<>();
            updatedLines.add(lines.get(0)); // Header

            for (int i = 1; i < lines.size(); i++) {
                String[] parts = parseCSVLine(lines.get(i));
                if (!parts[0].equals(expenseId)) {
                    updatedLines.add(lines.get(i));
                }
            }

            Files.write(userExpenseFile, updatedLines);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Save budget for a user
    public static boolean saveBudget(String username, Budget budget) {
        try {
            Path budgetDir = Paths.get(DATA_DIR, BUDGETS_DIR);
            Path userBudgetFile = budgetDir.resolve(username + ".csv");

            if (!Files.exists(userBudgetFile)) {
                Files.createFile(userBudgetFile);
                Files.write(userBudgetFile, "id,category,amount,period,createdDate\n".getBytes());
            }

            String line = String.format("%s,%s,%.2f,%s,%d%n",
                    budget.getId(),
                    budget.getCategory(),
                    budget.getAmount(),
                    budget.getPeriod(),
                    budget.getCreatedDate());

            Files.write(userBudgetFile, line.getBytes(),
                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Get all budgets for a user
    public static List<Budget> getBudgets(String username) {
        List<Budget> budgets = new ArrayList<>();
        try {
            Path userBudgetFile = Paths.get(DATA_DIR, BUDGETS_DIR, username + ".csv");
            if (!Files.exists(userBudgetFile)) {
                return budgets;
            }

            List<String> lines = Files.readAllLines(userBudgetFile);

            for (int i = 1; i < lines.size(); i++) {
                try {
                    String[] parts = parseCSVLine(lines.get(i));
                    if (parts.length >= 5) {
                        Budget budget = new Budget(
                                parts[0],
                                parts[1],
                                Double.parseDouble(parts[2]),
                                parts[3],
                                Long.parseLong(parts[4]));
                        budgets.add(budget);
                    }
                } catch (Exception e) {
                    // Skip malformed rows instead of interrupting the user's full budget list.
                }
            }
        } catch (Exception e) {
            return budgets;
        }
        return budgets;
    }

    // Delete a budget
    public static boolean deleteBudget(String username, String budgetId) {
        try {
            Path userBudgetFile = Paths.get(DATA_DIR, BUDGETS_DIR, username + ".csv");
            if (!Files.exists(userBudgetFile)) {
                return false;
            }

            List<String> lines = Files.readAllLines(userBudgetFile);
            List<String> updatedLines = new ArrayList<>();
            updatedLines.add(lines.get(0)); // Header

            for (int i = 1; i < lines.size(); i++) {
                String[] parts = parseCSVLine(lines.get(i));
                if (!parts[0].equals(budgetId)) {
                    updatedLines.add(lines.get(i));
                }
            }

            Files.write(userBudgetFile, updatedLines);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Parse CSV line handling quoted fields
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }

    // Sanitize CSV field to handle special characters
    private static String sanitizeCSVField(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // Simple password encryption (NOT FOR PRODUCTION - use BCrypt in real apps)
    private static String encryptPassword(String password) {
        StringBuilder sb = new StringBuilder();
        for (char c : password.toCharArray()) {
            sb.append((char) (c + 5));
        }
        return sb.toString();
    }

    // Simple password decryption
    private static String decryptPassword(String encrypted) {
        StringBuilder sb = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            sb.append((char) (c - 5));
        }
        return sb.toString();
    }
}
