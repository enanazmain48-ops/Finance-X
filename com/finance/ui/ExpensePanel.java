package com.finance.ui;

import com.finance.model.Expense;
import com.finance.util.CSVHandler;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpensePanel extends JPanel {
    private final String currentUser;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
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

    public ExpensePanel(String username, DashboardFrame parent) {
        this.currentUser = username;
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
        JLabel title = new JLabel("Manage Expenses");
        title.setFont(UIUtils.font(Font.BOLD, 30));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JComponent createInputPanel() {
        JPanel panel = UIUtils.createSurfacePanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(UIUtils.createCardBorder(24, 24, 24, 24));

        JLabel formTitle = new JLabel("Add Expense", SwingConstants.CENTER);
        formTitle.setFont(UIUtils.font(Font.BOLD, 20));
        formTitle.setForeground(UIUtils.TEXT_PRIMARY);
        panel.add(formTitle, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = UIUtils.createSectionLabel("Date");
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        UIUtils.styleSpinner(dateSpinner);

        JLabel amountLabel = UIUtils.createSectionLabel("Amount");
        JTextField amountField = new JTextField("0.00");
        UIUtils.styleTextField(amountField);

        JLabel categoryLabel = UIUtils.createSectionLabel("Category");
        JComboBox<String> categoryCombo = new JComboBox<>(CATEGORIES);
        UIUtils.styleComboBox(categoryCombo);

        JLabel descriptionLabel = UIUtils.createSectionLabel("Description");
        JTextArea descriptionArea = new JTextArea();
        UIUtils.styleTextArea(descriptionArea);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        UIUtils.styleScrollPane(descScroll, UIUtils.SURFACE_ALT);

        JButton addBtn = new JButton("Add Expense");
        UIUtils.stylePrimary(addBtn, UIUtils.SUCCESS);
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addExpense(dateSpinner, amountField, categoryCombo, descriptionArea));

        form.add(dateLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(dateSpinner);
        form.add(Box.createVerticalStrut(16));
        form.add(amountLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(amountField);
        form.add(Box.createVerticalStrut(16));
        form.add(categoryLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(categoryCombo);
        form.add(Box.createVerticalStrut(16));
        form.add(descriptionLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(descScroll);
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
        JLabel headingLabel = new JLabel("Expense History");
        headingLabel.setFont(UIUtils.font(Font.BOLD, 20));
        headingLabel.setForeground(UIUtils.TEXT_PRIMARY);
        heading.add(headingLabel, BorderLayout.WEST);

        String[] columnNames = { "Date", "Category", "Amount", "Description" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        UIUtils.styleTable(expenseTable);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        UIUtils.styleScrollPane(scrollPane, UIUtils.SURFACE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = new JButton("Delete Selected");
        UIUtils.styleSecondary(deleteBtn, UIUtils.DANGER);
        deleteBtn.addActionListener(e -> deleteSelectedExpense());
        buttonPanel.add(deleteBtn);

        JButton refreshBtn = new JButton("Refresh");
        UIUtils.styleSecondary(refreshBtn, UIUtils.PRIMARY);
        refreshBtn.addActionListener(e -> {
            expenses = CSVHandler.getExpenses(currentUser);
            refreshTable();
        });
        buttonPanel.add(refreshBtn);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addExpense(JSpinner dateSpinner, JTextField amountField,
            JComboBox<String> categoryCombo, JTextArea descriptionArea) {
        try {
            java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
            LocalDate date = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            double amount = Double.parseDouble(amountField.getText());
            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionArea.getText().trim();

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Validate against budgets for this category (monthly and weekly)
            java.util.List<com.finance.model.Budget> budgets = CSVHandler.getBudgets(currentUser);
            java.util.List<Expense> currentExpenses = CSVHandler.getExpenses(currentUser);

            for (com.finance.model.Budget budget : budgets) {
                if (!budget.getCategory().equals(category))
                    continue;

                if ("MONTHLY".equals(budget.getPeriod())) {
                    java.time.YearMonth ym = java.time.YearMonth.from(date);
                    double spent = currentExpenses.stream()
                            .filter(e2 -> e2.getCategory().equals(category)
                                    && java.time.YearMonth.from(e2.getDate()).equals(ym))
                            .mapToDouble(Expense::getAmount)
                            .sum();
                    if (spent + amount > budget.getAmount()) {
                        double remaining = budget.getAmount() - spent;
                        JOptionPane.showMessageDialog(this,
                                String.format("Cannot add expense: monthly budget for '%s' exceeded. Remaining: %.2f",
                                        category, remaining),
                                "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } else if ("WEEKLY".equals(budget.getPeriod())) {
                    java.time.LocalDate weekStart = date.minusDays(6);
                    double spent = currentExpenses.stream()
                            .filter(e2 -> e2.getCategory().equals(category) &&
                                    (!e2.getDate().isBefore(weekStart) && !e2.getDate().isAfter(date)))
                            .mapToDouble(Expense::getAmount)
                            .sum();
                    if (spent + amount > budget.getAmount()) {
                        double remaining = budget.getAmount() - spent;
                        JOptionPane.showMessageDialog(this,
                                String.format("Cannot add expense: weekly budget for '%s' exceeded. Remaining: %.2f",
                                        category, remaining),
                                "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            Expense expense = new Expense(date, amount, category, description);
            if (CSVHandler.saveExpense(currentUser, expense)) {
                JOptionPane.showMessageDialog(this, "Expense added successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                expenses = CSVHandler.getExpenses(currentUser);
                refreshTable();
                amountField.setText("0.00");
                descriptionArea.setText("");
                categoryCombo.setSelectedIndex(0);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRow < expenses.size()) {
            String expenseId = expenses.get(selectedRow).getId();
            if (CSVHandler.deleteExpense(currentUser, expenseId)) {
                JOptionPane.showMessageDialog(this, "Expense deleted successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                expenses = CSVHandler.getExpenses(currentUser);
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[] {
                    expense.getFormattedDate(),
                    expense.getCategory(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getDescription()
            });
        }
    }
}
