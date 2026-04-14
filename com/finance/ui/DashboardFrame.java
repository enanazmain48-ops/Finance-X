package com.finance.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class DashboardFrame extends JFrame {
    private final String currentUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton activeNavButton;
    private static final Color NAV_HOVER = new Color(77, 104, 150);

    public DashboardFrame(String username) {
        this.currentUser = username;
        try {
            setTitle("Finance X - Dashboard");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1380, 860);
            setMinimumSize(new Dimension(1160, 760));
            setLocationRelativeTo(null);
            setResizable(true);

            JPanel mainContainer = new JPanel(new BorderLayout(20, 0));
            mainContainer.setBackground(UIUtils.PAGE_BACKGROUND);
            mainContainer.setBorder(new EmptyBorder(18, 18, 18, 18));

            JPanel navPanel = createNavigationPanel();
            mainContainer.add(navPanel, BorderLayout.WEST);

            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);
            contentPanel.setBackground(UIUtils.PAGE_BACKGROUND);

            loadPanels();

            JPanel contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.setOpaque(false);
            contentWrapper.add(contentPanel, BorderLayout.CENTER);
            mainContainer.add(contentWrapper, BorderLayout.CENTER);

            add(mainContainer);
            setVisible(true);
            cardLayout.show(contentPanel, "DASHBOARD");

            if (activeNavButton != null) {
                highlightNavButton(activeNavButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create Dashboard: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPanels() {
        contentPanel.removeAll();
        try {
            contentPanel.add(new DashboardPanel(currentUser), "DASHBOARD");
        } catch (Exception e) {
            contentPanel.add(new JLabel("Error loading Dashboard"), "DASHBOARD");
        }

        try {
            contentPanel.add(new ExpensePanel(currentUser, this), "EXPENSES");
        } catch (Exception e) {
            contentPanel.add(new JLabel("Error loading Expenses"), "EXPENSES");
        }

        try {
            contentPanel.add(new BudgetPanel(currentUser, this), "BUDGETS");
        } catch (Exception e) {
            contentPanel.add(new JLabel("Error loading Budgets"), "BUDGETS");
        }

        try {
            contentPanel.add(new ReportPanel(currentUser), "REPORTS");
        } catch (Exception e) {
            contentPanel.add(new JLabel("Error loading Reports"), "REPORTS");
        }
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = UIUtils.createSoftPanel(new BorderLayout(), UIUtils.NAV_BACKGROUND);
        navPanel.setPreferredSize(new Dimension(250, 0));
        navPanel.setBorder(new EmptyBorder(26, 22, 26, 22));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel badge = UIUtils.createCapsuleLabel("FINANCE X", new Color(51, 83, 134), Color.WHITE);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(badge);
        topPanel.add(Box.createVerticalStrut(18));

        JLabel welcomeLabel = new JLabel("<html><div style='font-size:22px;font-weight:700;color:#ffffff;'>Welcome back</div>"
                + "<div style='font-size:14px;color:#d1def3;padding-top:6px;'>"
                + currentUser
                + "</div></html>");
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(welcomeLabel);
        topPanel.add(Box.createVerticalStrut(28));

        JPanel buttonGroup = new JPanel();
        buttonGroup.setOpaque(false);
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));

        buttonGroup.add(createNavButton("Dashboard", "DASHBOARD", true));
        buttonGroup.add(Box.createVerticalStrut(12));
        buttonGroup.add(createNavButton("Expenses", "EXPENSES", false));
        buttonGroup.add(Box.createVerticalStrut(12));
        buttonGroup.add(createNavButton("Budgets", "BUDGETS", false));
        buttonGroup.add(Box.createVerticalStrut(12));
        buttonGroup.add(createNavButton("Reports", "REPORTS", false));

        navPanel.add(topPanel, BorderLayout.NORTH);
        navPanel.add(buttonGroup, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        UIUtils.stylePrimary(logoutBtn, UIUtils.DANGER);
        logoutBtn.addActionListener(e -> handleLogout());
        navPanel.add(logoutBtn, BorderLayout.SOUTH);

        return navPanel;
    }

    private JButton createNavButton(String title, String panelName, boolean active) {
        JButton btn = new JButton();
        setNavButtonContent(btn, title, active);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btn.setPreferredSize(new Dimension(190, 56));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(UIUtils.font(Font.BOLD, 14));
        btn.setUI(new BasicButtonUI());
        UIUtils.styleNavigationButton(btn, new Insets(14, 18, 14, 18), 24);
        btn.setBackground(active ? UIUtils.NAV_ACTIVE : UIUtils.NAV_SURFACE);
        btn.setForeground(Color.WHITE);

        btn.addActionListener(e -> {
            if ("DASHBOARD".equals(panelName)) {
                loadPanels();
            }
            cardLayout.show(contentPanel, panelName);
            highlightNavButton(btn);
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeNavButton) {
                    btn.setBackground(NAV_HOVER);
                    setNavButtonContent(btn, title, false);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeNavButton) {
                    btn.setBackground(UIUtils.NAV_SURFACE);
                    setNavButtonContent(btn, title, false);
                }
            }
        });

        if (active) {
            activeNavButton = btn;
        }

        return btn;
    }

    private void highlightNavButton(JButton selectedButton) {
        if (activeNavButton != null && activeNavButton != selectedButton) {
            activeNavButton.setBackground(UIUtils.NAV_SURFACE);
            String text = getNavButtonText(activeNavButton);
            setNavButtonContent(activeNavButton, text, false);
        }
        activeNavButton = selectedButton;
        activeNavButton.setBackground(UIUtils.NAV_ACTIVE);
        String text = getNavButtonText(activeNavButton);
        setNavButtonContent(activeNavButton, text, true);
    }

    private void setNavButtonContent(JButton button, String title, boolean active) {
        button.putClientProperty("navTitle", title);
        String titleColor = active ? "#ffffff" : "#f7fbff";
        if (NAV_HOVER.equals(button.getBackground())) {
            titleColor = "#1b263b";
        }
        button.setText("<html><div style='font-family:Segoe UI,sans-serif;'>"
                + "<div style='font-size:15px;font-weight:700;color:" + titleColor + ";'>" + title + "</div>"
                + "</div></html>");
    }

    private String getNavButtonText(JButton button) {
        return String.valueOf(button.getClientProperty("navTitle"));
    }

    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            new LoginFrame();
            dispose();
        }
    }

    public void refreshContent(String panelName) {
        if ("DASHBOARD".equals(panelName)) {
            loadPanels();
        }
        cardLayout.show(contentPanel, panelName);
    }
}
