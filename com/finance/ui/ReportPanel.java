package com.finance.ui;

import com.finance.model.Budget;
import com.finance.model.Expense;
import com.finance.util.CSVHandler;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class ReportPanel extends JPanel {
    private final List<Expense> expenses;
    private final List<Budget> budgets;
    private final JPanel chartPanel;
    private JButton activeReportButton;

    public ReportPanel(String username) {
        this.expenses = CSVHandler.getExpenses(username);
        this.budgets = CSVHandler.getBudgets(username);
        this.chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);

        setLayout(new BorderLayout(0, 24));
        setBackground(UIUtils.PAGE_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);

        showCategoryDistribution();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel title = new JLabel("Financial Reports");
        title.setFont(UIUtils.font(Font.BOLD, 30));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JPanel createBodyPanel() {
        JPanel body = new JPanel(new BorderLayout(24, 0));
        body.setOpaque(false);
        body.add(createControlPanel(), BorderLayout.WEST);

        JPanel chartCard = UIUtils.createSurfacePanel(new BorderLayout());
        chartCard.setBorder(UIUtils.createCardBorder(24, 24, 24, 24));
        chartCard.add(chartPanel, BorderLayout.CENTER);
        body.add(chartCard, BorderLayout.CENTER);

        return body;
    }

    private JComponent createControlPanel() {
        JPanel panel = UIUtils.createSurfacePanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(UIUtils.createCardBorder(24, 24, 24, 24));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel reportTitle = new JLabel("Report Views");
        reportTitle.setFont(UIUtils.font(Font.BOLD, 20));
        reportTitle.setForeground(UIUtils.TEXT_PRIMARY);
        content.add(reportTitle);
        content.add(Box.createVerticalStrut(18));

        content.add(createReportButton("Category Distribution", "CATEGORY_DISTRIBUTION", true));
        content.add(Box.createVerticalStrut(12));
        content.add(createReportButton("Monthly Trend", "MONTHLY_TREND", false));
        content.add(Box.createVerticalStrut(12));
        content.add(createReportButton("Budget Adherence", "BUDGET_ADHERENCE", false));
        content.add(Box.createVerticalStrut(12));
        content.add(createReportButton("Spending by Period", "PERIOD_SPENDING", false));
        content.add(Box.createVerticalGlue());

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JButton createReportButton(String title, String reportType, boolean active) {
        JButton btn = new JButton(title);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        btn.setPreferredSize(new Dimension(210, 52));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new BasicButtonUI());
        UIUtils.styleNavigationButton(btn, new Insets(12, 18, 12, 18), 22);
        btn.setBackground(active ? UIUtils.PRIMARY : UIUtils.PRIMARY_SOFT);
        btn.setForeground(active ? Color.WHITE : UIUtils.TEXT_PRIMARY);
        btn.setFont(UIUtils.font(Font.BOLD, 14));

        btn.addActionListener(e -> {
            setActiveReportButton(btn);
            showReport(reportType);
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeReportButton) {
                    btn.setBackground(new Color(234, 242, 252));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeReportButton) {
                    btn.setBackground(UIUtils.PRIMARY_SOFT);
                }
            }
        });

        if (active) {
            activeReportButton = btn;
        }

        return btn;
    }

    private void setActiveReportButton(JButton selected) {
        if (activeReportButton != null && activeReportButton != selected) {
            activeReportButton.setBackground(UIUtils.PRIMARY_SOFT);
            activeReportButton.setForeground(UIUtils.TEXT_PRIMARY);
        }
        activeReportButton = selected;
        activeReportButton.setBackground(UIUtils.PRIMARY);
        activeReportButton.setForeground(Color.WHITE);
    }

    private void showReport(String reportType) {
        switch (reportType) {
            case "CATEGORY_DISTRIBUTION":
                showCategoryDistribution();
                break;
            case "MONTHLY_TREND":
                showMonthlyTrend();
                break;
            case "BUDGET_ADHERENCE":
                showBudgetAdherence();
                break;
            case "PERIOD_SPENDING":
                showPeriodSpending();
                break;
            default:
                showCategoryDistribution();
                break;
        }
    }

    private void showCategoryDistribution() {
        chartPanel.removeAll();

        Map<String, Double> categoryTotals = new TreeMap<>();
        for (Expense expense : expenses) {
            categoryTotals.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        if (categoryTotals.isEmpty()) {
            chartPanel.add(createEmptyState("No expense data available yet."), BorderLayout.CENTER);
        } else {
            chartPanel.add(new PieChart("Category Distribution", categoryTotals), BorderLayout.CENTER);
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void showMonthlyTrend() {
        chartPanel.removeAll();

        Map<String, Double> monthlyTotals = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            monthlyTotals.put(month.toString(), 0.0);
        }

        for (Expense expense : expenses) {
            YearMonth month = YearMonth.from(expense.getDate());
            String monthStr = month.toString();
            if (monthlyTotals.containsKey(monthStr)) {
                monthlyTotals.put(monthStr, monthlyTotals.get(monthStr) + expense.getAmount());
            }
        }

        chartPanel.add(new BarChart("Monthly Spending Trend", monthlyTotals), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void showBudgetAdherence() {
        chartPanel.removeAll();

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Budget Adherence");
        title.setFont(UIUtils.font(Font.BOLD, 24));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(8));
        listPanel.add(UIUtils.createMutedLabel("See how each category is performing against its limit."));
        listPanel.add(Box.createVerticalStrut(22));

        if (budgets.isEmpty()) {
            listPanel.add(createEmptyState("No budgets set yet."));
        } else {
            for (Budget budget : budgets) {
                listPanel.add(createBudgetAdherencePanel(budget));
                listPanel.add(Box.createVerticalStrut(16));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UIUtils.SURFACE);
        scrollPane.setBackground(UIUtils.SURFACE);

        chartPanel.add(scrollPane, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private JPanel createBudgetAdherencePanel(Budget budget) {
        JPanel panel = UIUtils.createSoftPanel(new BorderLayout(18, 0), UIUtils.SURFACE_ALT);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(budget.getCategory() + " (" + budget.getPeriod() + ")");
        titleLabel.setFont(UIUtils.font(Font.BOLD, 16));
        titleLabel.setForeground(UIUtils.TEXT_PRIMARY);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(8));

        double spent = calculateSpentAmount(budget);
        double budgetAmount = budget.getAmount();
        double percentage = (spent / budgetAmount) * 100;

        JLabel amountLabel = new JLabel(String.format("Budget: %.2f   Spent: %.2f   Remaining: %.2f",
                budgetAmount, spent, Math.max(0, budgetAmount - spent)));
        amountLabel.setFont(UIUtils.font(Font.PLAIN, 14));
        amountLabel.setForeground(UIUtils.TEXT_MUTED);
        infoPanel.add(amountLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) Math.min(100, percentage));
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.1f%%", percentage));
        progressBar.setFont(UIUtils.font(Font.BOLD, 13));
        progressBar.setPreferredSize(new Dimension(240, 26));
        progressBar.setBackground(new Color(232, 238, 246));

        if (percentage <= 80) {
            progressBar.setForeground(UIUtils.SUCCESS);
        } else if (percentage <= 100) {
            progressBar.setForeground(UIUtils.WARNING);
        } else {
            progressBar.setForeground(UIUtils.DANGER);
        }

        JPanel progressPanel = new JPanel(new GridBagLayout());
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar);
        panel.add(progressPanel, BorderLayout.EAST);

        return panel;
    }

    private void showPeriodSpending() {
        chartPanel.removeAll();

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        LocalDate fourteenDaysAgo = today.minusDays(14);

        double thisWeek = expenses.stream()
                .filter(e -> e.getDate().isAfter(sevenDaysAgo))
                .mapToDouble(Expense::getAmount)
                .sum();

        double lastWeek = expenses.stream()
                .filter(e -> e.getDate().isAfter(fourteenDaysAgo) && e.getDate().isBefore(sevenDaysAgo))
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> weekData = new LinkedHashMap<>();
        weekData.put("This Week", thisWeek);
        weekData.put("Last Week", lastWeek);

        chartPanel.add(new BarChart("Weekly Spending Comparison", weekData), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private JComponent createEmptyState(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(message);
        label.setFont(UIUtils.font(Font.PLAIN, 15));
        label.setForeground(UIUtils.TEXT_MUTED);
        panel.add(label);
        return panel;
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

    private static class PieChart extends JPanel {
        private final String title;
        private final Map<String, Double> data;

        PieChart(String title, Map<String, Double> data) {
            this.title = title;
            this.data = data;
            setOpaque(false);
            setPreferredSize(new Dimension(820, 520));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int legendWidth = 260;

            g2d.setFont(UIUtils.font(Font.BOLD, 24));
            g2d.setColor(UIUtils.TEXT_PRIMARY);
            g2d.drawString(title, 26, 38);

            double total = data.values().stream().mapToDouble(Double::doubleValue).sum();
            if (total == 0) {
                g2d.dispose();
                return;
            }

            int chartAreaWidth = Math.max(220, width - legendWidth - 80);
            int diameter = Math.min(chartAreaWidth, height - 130);
            int x = 40;
            int y = Math.max(70, (height - diameter) / 2);

            int startAngle = 0;
            int colorIndex = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int angle = (int) Math.round((entry.getValue() / total) * 360);
                g2d.setColor(UIUtils.CHART_COLORS[colorIndex % UIUtils.CHART_COLORS.length]);
                g2d.fillArc(x, y, diameter, diameter, startAngle, angle);
                startAngle += angle;
                colorIndex++;
            }

            g2d.setColor(UIUtils.SURFACE);
            int innerDiameter = (int) (diameter * 0.48);
            int innerX = x + (diameter - innerDiameter) / 2;
            int innerY = y + (diameter - innerDiameter) / 2;
            g2d.fillOval(innerX, innerY, innerDiameter, innerDiameter);

            g2d.setColor(UIUtils.TEXT_MUTED);
            g2d.setFont(UIUtils.font(Font.PLAIN, 14));
            g2d.drawString("Total spend", innerX + 34, innerY + innerDiameter / 2 - 8);
            g2d.setColor(UIUtils.TEXT_PRIMARY);
            g2d.setFont(UIUtils.font(Font.BOLD, 22));
            g2d.drawString(String.format("%.2f", total), innerX + 24, innerY + innerDiameter / 2 + 22);

            int legendX = width - legendWidth;
            int legendY = 96;
            colorIndex = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                Color color = UIUtils.CHART_COLORS[colorIndex % UIUtils.CHART_COLORS.length];
                double percentage = (entry.getValue() / total) * 100;

                g2d.setColor(color);
                g2d.fillRoundRect(legendX, legendY, 16, 16, 6, 6);

                g2d.setColor(UIUtils.TEXT_PRIMARY);
                g2d.setFont(UIUtils.font(Font.BOLD, 14));
                g2d.drawString(entry.getKey(), legendX + 28, legendY + 13);

                g2d.setColor(UIUtils.TEXT_MUTED);
                g2d.setFont(UIUtils.font(Font.PLAIN, 13));
                g2d.drawString(String.format("%.2f (%.1f%%)", entry.getValue(), percentage), legendX + 28, legendY + 33);

                legendY += 52;
                colorIndex++;
            }

            g2d.dispose();
        }
    }

    private static class BarChart extends JPanel {
        private final String title;
        private final Map<String, Double> data;

        BarChart(String title, Map<String, Double> data) {
            this.title = title;
            this.data = data;
            setOpaque(false);
            setPreferredSize(new Dimension(860, 520));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int left = 70;
            int right = width - 40;
            int top = 80;
            int bottom = height - 70;

            g2d.setColor(UIUtils.TEXT_PRIMARY);
            g2d.setFont(UIUtils.font(Font.BOLD, 24));
            g2d.drawString(title, 26, 38);

            if (data.isEmpty()) {
                g2d.dispose();
                return;
            }

            double maxValue = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
            if (maxValue == 0) {
                maxValue = 1;
            }

            g2d.setFont(UIUtils.font(Font.PLAIN, 12));
            for (int i = 0; i <= 4; i++) {
                int y = bottom - (int) ((bottom - top) * (i / 4.0));
                g2d.setColor(new Color(229, 235, 243));
                g2d.drawLine(left, y, right, y);

                g2d.setColor(UIUtils.TEXT_MUTED);
                double value = (maxValue / 4.0) * i;
                g2d.drawString(String.format("%.0f", value), 22, y + 4);
            }

            int count = data.size();
            int availableWidth = right - left;
            int slotWidth = availableWidth / Math.max(1, count);
            int barWidth = Math.min(80, slotWidth - 24);
            int x = left + (slotWidth - barWidth) / 2;

            int colorIndex = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                double percentage = entry.getValue() / maxValue;
                int barHeight = (int) ((bottom - top - 10) * percentage);
                int y = bottom - barHeight;

                Color barColor = UIUtils.CHART_COLORS[colorIndex % UIUtils.CHART_COLORS.length];
                g2d.setColor(barColor);
                g2d.fillRoundRect(x, y, barWidth, barHeight, 18, 18);

                g2d.setColor(UIUtils.TEXT_PRIMARY);
                g2d.setFont(UIUtils.font(Font.BOLD, 13));
                g2d.drawString(String.format("%.2f", entry.getValue()), x - 4, y - 10);

                g2d.setFont(UIUtils.font(Font.PLAIN, 13));
                FontMetrics metrics = g2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(entry.getKey());
                g2d.drawString(entry.getKey(), x + (barWidth - labelWidth) / 2, bottom + 24);

                x += slotWidth;
                colorIndex++;
            }

            g2d.dispose();
        }
    }
}
