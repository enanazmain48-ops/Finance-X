package com.finance.ui;

import com.finance.model.Budget;
import com.finance.model.Expense;
import com.finance.util.CSVHandler;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BudgetPanel extends JPanel {
    private final String currentUser;
    private JTable budgetTable;
    private DefaultTableModel tableModel;
    private List<Budget> budgets;
    private List<Expense> expenses;

    private final String[] CATEGORIES = {
            "Food & Dining",
            "Transportation",
            "Shopping",
            "Entertainment",
            "Utilities",
            "Health & Fitness",
            "Education",
            "Other"
    };

    private final String[] PERIODS = { "MONTHLY", "WEEKLY" };

    public BudgetPanel(String username, DashboardFrame parent) {
        this.currentUser = username;
        this.budgets = CSVHandler.getBudgets(username);
        this.expenses = CSVHandler.getExpenses(username);

        setLayout(new BorderLayout(0, 24));
        setBackground(UIUtils.PAGE_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(24, 0));
        content.setOpaque(false);
        content.add(createInputPanel(), BorderLayout.WEST);
        content.add(createTablePanel(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel title = new JLabel("Manage Budgets");
        title.setFont(UIUtils.font(Font.BOLD, 30));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JComponent createInputPanel() {
        JPanel panel = UIUtils.createSurfacePanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(UIUtils.createCardBorder(24, 24, 24, 24));

        JLabel formTitle = new JLabel("Set Budget", SwingConstants.CENTER);
        formTitle.setFont(UIUtils.font(Font.BOLD, 20));
        formTitle.setForeground(UIUtils.TEXT_PRIMARY);
        panel.add(formTitle, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> categoryCombo = new JComboBox<>(CATEGORIES);
        UIUtils.styleComboBox(categoryCombo);

        JTextField amountField = new JTextField("0.00");
        UIUtils.styleTextField(amountField);

        JComboBox<String> periodCombo = new JComboBox<>(PERIODS);
        UIUtils.styleComboBox(periodCombo);

        JButton addBtn = new JButton("Set Budget");
        UIUtils.stylePrimary(addBtn, new Color(105, 94, 200));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addBudget(categoryCombo, amountField, periodCombo));

        form.add(UIUtils.createSectionLabel("Category"));
        form.add(Box.createVerticalStrut(8));
        form.add(categoryCombo);
        form.add(Box.createVerticalStrut(16));
        form.add(UIUtils.createSectionLabel("Budget Amount"));
        form.add(Box.createVerticalStrut(8));
        form.add(amountField);
        form.add(Box.createVerticalStrut(16));
        form.add(UIUtils.createSectionLabel("Period"));
        form.add(Box.createVerticalStrut(8));
        form.add(periodCombo);
        form.add(Box.createVerticalStrut(20));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.add(addBtn);
        form.add(buttonRow);
        form.add(Box.createVerticalGlue());

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = UIUtils.createSurfacePanel(new BorderLayout(0, 16));
        panel.setBorder(UIUtils.createCardBorder(24, 24, 24, 24));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        JLabel headingLabel = new JLabel("Budget Overview");
        headingLabel.setFont(UIUtils.font(Font.BOLD, 20));
        headingLabel.setForeground(UIUtils.TEXT_PRIMARY);
        heading.add(headingLabel, BorderLayout.WEST);

        String[] columnNames = { "Category", "Budget", "Spent", "Period", "Progress %" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        budgetTable = new JTable(tableModel);
        UIUtils.styleTable(budgetTable);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(budgetTable);
        UIUtils.styleScrollPane(scrollPane, UIUtils.SURFACE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = new JButton("Delete Selected");
        UIUtils.styleSecondary(deleteBtn, UIUtils.DANGER);
        deleteBtn.addActionListener(e -> deleteSelectedBudget());
        buttonPanel.add(deleteBtn);

        JButton refreshBtn = new JButton("Refresh");
        UIUtils.styleSecondary(refreshBtn, UIUtils.PRIMARY);
        refreshBtn.addActionListener(e -> {
            budgets = CSVHandler.getBudgets(currentUser);
            expenses = CSVHandler.getExpenses(currentUser);
            refreshTable();
        });
        buttonPanel.add(refreshBtn);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addBudget(JComboBox<String> categoryCombo, JTextField amountField, JComboBox<String> periodCombo) {
        try {
            String category = (String) categoryCombo.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());
            String period = (String) periodCombo.getSelectedItem();

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exists = budgets.stream()
                    .anyMatch(b -> b.getCategory().equals(category) && b.getPeriod().equals(period));

            if (exists) {
                JOptionPane.showMessageDialog(this, "Budget already exists for this category and period",
                        "Duplicate Budget", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Budget budget = new Budget(category, amount, period);
            if (CSVHandler.saveBudget(currentUser, budget)) {
                JOptionPane.showMessageDialog(this, "Budget set successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                budgets = CSVHandler.getBudgets(currentUser);
                refreshTable();
                amountField.setText("0.00");
                categoryCombo.setSelectedIndex(0);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedBudget() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a budget to delete",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRow < budgets.size()) {
            String budgetId = budgets.get(selectedRow).getId();
            if (CSVHandler.deleteBudget(currentUser, budgetId)) {
                JOptionPane.showMessageDialog(this, "Budget deleted successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                budgets = CSVHandler.getBudgets(currentUser);
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Budget budget : budgets) {
            double spent = calculateSpentAmount(budget);
            double percentage = (spent / budget.getAmount()) * 100;

            tableModel.addRow(new Object[] {
                    budget.getCategory(),
                    String.format("%.2f", budget.getAmount()),
                    String.format("%.2f", spent),
                    budget.getPeriod(),
                    String.format("%.1f%%", Math.min(percentage, 100))
            });
        }
    }

    private double calculateSpentAmount(Budget budget) {
        if (budget.getPeriod().equals("MONTHLY")) {
            YearMonth currentMonth = YearMonth.now();
            return expenses.stream()
                    .filter(e -> e.getCategory().equals(budget.getCategory()) &&
                            YearMonth.from(e.getDate()).equals(currentMonth))
                    .mapToDouble(Expense::getAmount)
                    .sum();
        } else {
            LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
            return expenses.stream()
                    .filter(e -> e.getCategory().equals(budget.getCategory()) &&
                            e.getDate().isAfter(sevenDaysAgo))
                    .mapToDouble(Expense::getAmount)
                    .sum();
        }
    }
}
