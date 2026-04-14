package com.finance.ui;

import com.finance.model.User;
import com.finance.util.CSVHandler;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private final JPanel mainPanel;
    private final JPanel loginPanel;
    private final JPanel registerPanel;
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JTextField regUsername;
    private JTextField regEmail;
    private JPasswordField regPassword;
    private JPasswordField regConfirmPassword;

    public LoginFrame() {
        try {
            setTitle("Finance X - Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1040, 720);
            setMinimumSize(new Dimension(980, 680));
            setLocationRelativeTo(null);
            setResizable(true);

            JPanel rootPanel = createBackgroundPanel();
            rootPanel.setLayout(new GridBagLayout());
            rootPanel.setBorder(new EmptyBorder(28, 28, 28, 28));

            JPanel shell = UIUtils.createSurfacePanel(new GridBagLayout());
            shell.setPreferredSize(new Dimension(920, 580));

            mainPanel = new JPanel(new CardLayout());
            mainPanel.setOpaque(false);

            loginPanel = createLoginPanel();
            registerPanel = createRegisterPanel();

            mainPanel.add(loginPanel, "LOGIN");
            mainPanel.add(registerPanel, "REGISTER");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;

            gbc.gridx = 0;
            gbc.weightx = 0.58;
            gbc.insets = new Insets(0, 0, 0, 18);
            shell.add(createBrandPanel(), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.42;
            gbc.insets = new Insets(0, 0, 0, 0);
            shell.add(mainPanel, gbc);

            rootPanel.add(shell);
            setContentPane(rootPanel);

            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize LoginFrame", e);
        }
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint paint = new GradientPaint(0, 0, new Color(232, 241, 252),
                        getWidth(), getHeight(), new Color(248, 251, 255));
                g2d.setPaint(paint);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(202, 223, 245, 110));
                g2d.fillOval(-80, -40, 320, 320);
                g2d.setColor(new Color(218, 235, 251, 130));
                g2d.fillOval(getWidth() - 280, getHeight() - 260, 360, 360);
                g2d.dispose();
            }
        };
    }

    private JPanel createBrandPanel() {
        JPanel panel = UIUtils.createSoftPanel(new BorderLayout(), new Color(24, 47, 82));
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel badge = UIUtils.createCapsuleLabel("FINANCE X", new Color(52, 86, 138), Color.WHITE);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(badge);
        content.add(Box.createVerticalStrut(22));

        JLabel title = new JLabel(
                "<html><div style='width:360px;font-size:30px;font-weight:700;line-height:1.15;color:#ffffff;'>"
                        + "Track money with a calmer, clearer workspace."
                        + "</div></html>");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(14));

        JLabel subtitle = new JLabel("<html><div style='width:360px;font-size:14px;color:#d6e5fb;line-height:1.45;'>"
                + "Manage expenses, budgets, and reports from one readable dashboard with bigger controls and cleaner structure."
                + "</div></html>");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createVerticalGlue());

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome back");
        title.setFont(UIUtils.font(Font.BOLD, 28));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        JLabel subtitle = new JLabel("Sign in to continue managing your money.");
        subtitle.setFont(UIUtils.font(Font.PLAIN, 15));
        subtitle.setForeground(UIUtils.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(20));

        loginUsername = createTextField();
        loginPassword = createPasswordField();

        content.add(createFieldBlock("Username", wrapAuthField(loginUsername)));
        content.add(Box.createVerticalStrut(12));
        content.add(createPasswordBlock("Password", loginPassword));
        content.add(Box.createVerticalStrut(18));

        JButton loginBtn = createStyledButton("Login", UIUtils.PRIMARY);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        content.add(loginBtn);
        content.add(Box.createVerticalStrut(12));

        JButton registerLinkBtn = new JButton("Need an account? Create one");
        UIUtils.styleLinkButton(registerLinkBtn);
        registerLinkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerLinkBtn.addActionListener(e -> showRegisterPanel());
        content.add(registerLinkBtn);
        content.add(Box.createVerticalGlue());

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 16, 12, 12));

        // Create a vertically stacked form that can scroll when space is tight
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create your account");
        title.setFont(UIUtils.font(Font.BOLD, 24));
        title.setForeground(UIUtils.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);

        // Small gap between title and first field
        form.add(Box.createVerticalStrut(14));

        regUsername = createTextField();
        regEmail = createTextField();
        regPassword = createPasswordField();
        regConfirmPassword = createPasswordField();

        form.add(createFieldBlock("Username", wrapAuthField(regUsername)));
        form.add(Box.createVerticalStrut(8));
        form.add(createFieldBlock("Email", wrapAuthField(regEmail)));
        form.add(Box.createVerticalStrut(8));
        form.add(createPasswordBlock("Password", regPassword));
        form.add(Box.createVerticalStrut(8));
        form.add(createPasswordBlock("Confirm Password", regConfirmPassword));
        form.add(Box.createVerticalStrut(12));

        JButton registerBtn = createStyledButton("Register", UIUtils.SUCCESS);
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registerBtn.addActionListener(e -> handleRegister());
        form.add(registerBtn);
        form.add(Box.createVerticalStrut(8));
        // Add the form directly (no scroll) for a compact layout
        panel.add(form, BorderLayout.CENTER);

        // Footer with fixed back-to-login link so it is always visible
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel backLinkLabel = new JLabel("<html><u>Already have an account? Sign in</u></html>");
        backLinkLabel.setFont(UIUtils.font(Font.BOLD, 14));
        backLinkLabel.setForeground(UIUtils.TEXT_PRIMARY);
        backLinkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLinkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showLoginPanel();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backLinkLabel.setForeground(UIUtils.PRIMARY.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backLinkLabel.setForeground(UIUtils.TEXT_PRIMARY);
            }
        });

        footer.add(backLinkLabel);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFieldBlock(String labelText, JComponent field) {
        JPanel block = new JPanel();
        block.setOpaque(false);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.font(Font.BOLD, 13));
        label.setForeground(UIUtils.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        block.add(label);
        block.add(Box.createVerticalStrut(6));
        block.add(field);
        return block;
    }

    private JPanel createPasswordBlock(String labelText, JPasswordField field) {
        JPanel block = createFieldBlock(labelText, wrapAuthField(field));
        block.add(Box.createVerticalStrut(6));

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setForeground(UIUtils.TEXT_MUTED);
        showPassword.setFont(UIUtils.font(Font.PLAIN, 13));
        showPassword.setFocusPainted(false);
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        char defaultEchoChar = field.getEchoChar();
        showPassword.addActionListener(e -> field.setEchoChar(showPassword.isSelected() ? (char) 0 : defaultEchoChar));
        block.add(showPassword);
        return block;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        styleAuthField(tf);
        return tf;
    }

    private JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        styleAuthField(pf);
        return pf;
    }

    private void styleAuthField(JTextField field) {
        field.setFont(UIUtils.font(Font.PLAIN, 15));
        field.setForeground(UIUtils.TEXT_PRIMARY);
        field.setCaretColor(UIUtils.PRIMARY);
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(0, 0, 0, 0));
        field.setFont(UIUtils.font(Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        field.setPreferredSize(new Dimension(Math.max(field.getPreferredSize().width, 220), 20));
    }

    private JComponent wrapAuthField(JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 0, 0));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        wrapper.setPreferredSize(new Dimension(Math.max(field.getPreferredSize().width, 220), 36));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent surface = UIUtils.createSoftPanel(new BorderLayout(), new Color(252, 253, 255));
        surface.setBorder(new EmptyBorder(10, 14, 10, 14));
        surface.add(field, BorderLayout.CENTER);

        wrapper.add(surface, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        UIUtils.stylePrimary(btn, color);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return btn;
    }

    private void handleLogin() {
        try {
            String username = loginUsername.getText().trim();
            String password = new String(loginPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = CSVHandler.getUser(username);

            if (user != null && user.getPassword().equals(password)) {
                new DashboardFrame(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                loginPassword.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        try {
            String username = regUsername.getText().trim();
            String email = regEmail.getText().trim();
            String password = new String(regPassword.getPassword());
            String confirmPassword = new String(regConfirmPassword.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (CSVHandler.userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(username, password, email);
            if (CSVHandler.saveUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                showLoginPanel();
                regUsername.setText("");
                regEmail.setText("");
                regPassword.setText("");
                regConfirmPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "LOGIN");
    }

    private void showRegisterPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "REGISTER");
    }
}
