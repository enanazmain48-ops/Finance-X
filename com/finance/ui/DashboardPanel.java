package com.finance.ui;

import com.finance.model.Expense;
import com.finance.util.CSVHandler;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;

public class DashboardPanel extends JPanel {
    private final List<Expense> expenses;

    public DashboardPanel(String username) {
        this.expenses = CSVHandler.getExpenses(username);

        setLayout(new BorderLayout(0, 24));
        setBackground(UIUtils.PAGE_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createStatsPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel introPanel = UIUtils.createSurfacePanel(new BorderLayout(24, 0));
        introPanel.setBorder(UIUtils.createCardBorder(24, 28, 24, 28));

        JLabel titleLabel = UIUtils.createPageTitle("Dashboard", "A clearer snapshot of your recent financial activity.");
        introPanel.add(titleLabel, BorderLayout.WEST);

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        JLabel totalLabel = new JLabel("<html><div style='font-size:13px;color:#6a768a;'>All-time spend</div>"
                + "<div style='font-size:28px;font-weight:700;color:#2271c4;padding-top:6px;'>"
                + String.format("%.2f", totalExpenses)
                + "</div></html>");
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        introPanel.add(totalLabel, BorderLayout.EAST);

        panel.add(introPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 24, 24));
        panel.setOpaque(false);

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double thisMonthExpenses = calculateMonthExpenses(YearMonth.now());
        double thisWeekExpenses = calculateWeekExpenses();
        int expenseCount = expenses.size();

        panel.add(createStatCard(
                "Total Expenses",
                String.format("%.2f", totalExpenses),
                "Across your full history",
                UIUtils.DANGER));

        panel.add(createStatCard(
                "This Month",
                String.format("%.2f", thisMonthExpenses),
                "Current month spending",
                UIUtils.PRIMARY));

        panel.add(createStatCard(
                "This Week",
                String.format("%.2f", thisWeekExpenses),
                "Rolling last 7 days",
                UIUtils.SUCCESS));

        panel.add(createStatCard(
                "Transactions",
                String.valueOf(expenseCount),
                "Recorded expense entries",
                UIUtils.WARNING));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = UIUtils.createSurfacePanel(new BorderLayout());
        card.setBorder(UIUtils.createCardBorder(22, 22, 22, 22));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.font(Font.BOLD, 18));
        titleLabel.setForeground(UIUtils.TEXT_PRIMARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIUtils.font(Font.BOLD, 34));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UIUtils.font(Font.PLAIN, 14));
        subtitleLabel.setForeground(UIUtils.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(Box.createVerticalGlue());
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(28));
        content.add(valueLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitleLabel);
        content.add(Box.createVerticalGlue());

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private double calculateMonthExpenses(YearMonth month) {
        return expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    private double calculateWeekExpenses() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return expenses.stream()
                .filter(e -> e.getDate().isAfter(sevenDaysAgo))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
