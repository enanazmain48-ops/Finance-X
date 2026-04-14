package com.finance.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

public final class UIUtils {
    public static final Color PAGE_BACKGROUND = new Color(240, 245, 252);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(246, 250, 255);
    public static final Color BORDER = new Color(214, 224, 238);
    public static final Color TEXT_PRIMARY = new Color(27, 38, 59);
    public static final Color TEXT_MUTED = new Color(106, 118, 138);
    public static final Color PRIMARY = new Color(34, 113, 196);
    public static final Color PRIMARY_DARK = new Color(23, 84, 148);
    public static final Color PRIMARY_SOFT = new Color(225, 238, 252);
    public static final Color SUCCESS = new Color(33, 158, 108);
    public static final Color SUCCESS_SOFT = new Color(225, 246, 237);
    public static final Color DANGER = new Color(201, 74, 96);
    public static final Color DANGER_SOFT = new Color(252, 235, 239);
    public static final Color WARNING = new Color(224, 156, 59);
    public static final Color WARNING_SOFT = new Color(253, 243, 223);
    public static final Color NAV_BACKGROUND = new Color(18, 34, 61);
    public static final Color NAV_SURFACE = new Color(31, 52, 88);
    public static final Color NAV_ACTIVE = new Color(71, 123, 205);
    public static final Color[] CHART_COLORS = {
            new Color(49, 130, 206),
            new Color(38, 166, 154),
            new Color(245, 166, 35),
            new Color(228, 99, 111),
            new Color(111, 137, 250),
            new Color(120, 190, 91),
            new Color(249, 132, 74),
            new Color(123, 104, 238)
    };

    private static boolean defaultsConfigured;
    private static final int BUTTON_ARC = 28;
    private static final int FIELD_ARC = 20;

    private UIUtils() {
    }

    public static void configureDefaults() {
        if (defaultsConfigured) {
            return;
        }

        FontUIResource regular = new FontUIResource(font(Font.PLAIN, 14));
        FontUIResource heading = new FontUIResource(font(Font.BOLD, 14));

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, regular);
            }
        }

        UIManager.put("Button.font", heading);
        UIManager.put("Label.font", regular);
        UIManager.put("TextField.font", regular);
        UIManager.put("PasswordField.font", regular);
        UIManager.put("TextArea.font", regular);
        UIManager.put("ComboBox.font", regular);
        UIManager.put("Spinner.font", regular);
        UIManager.put("Table.font", regular);
        UIManager.put("TableHeader.font", heading);
        UIManager.put("OptionPane.messageFont", regular);
        UIManager.put("OptionPane.buttonFont", heading);
        UIManager.put("ToolTip.font", regular);
        UIManager.put("ScrollBar.width", 12);

        defaultsConfigured = true;
    }

    public static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static JPanel createSurfacePanel(LayoutManager layout) {
        RoundedPanel panel = new RoundedPanel(layout, SURFACE, 28);
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));
        return panel;
    }

    public static JPanel createSoftPanel(LayoutManager layout, Color background) {
        RoundedPanel panel = new RoundedPanel(layout, background, 24);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    public static Border createCardBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static JLabel createPageTitle(String title, String subtitle) {
        JLabel label = new JLabel("<html><div style='font-size:30px;font-weight:700;color:#1b263b;'>"
                + title
                + "</div><div style='font-size:14px;color:#6a768a;padding-top:6px;'>"
                + subtitle
                + "</div></html>");
        return label;
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.PLAIN, 13));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    public static JLabel createCapsuleLabel(String text, Color background, Color foreground) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(background);
        label.setForeground(foreground);
        label.setFont(font(Font.BOLD, 12));
        label.setBorder(new CompoundBorder(new LineBorder(background.darker(), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        return label;
    }

    public static void stylePrimary(JButton btn, Color bg) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBackground(bg);
        btn.setForeground(getContrastingColor(bg));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(font(Font.BOLD, 14));
        btn.setUI(new RoundedButtonUI(BUTTON_ARC));
        btn.setBorder(new RoundedButtonBorder(softStroke(bg), BUTTON_ARC, new Insets(12, 20, 12, 20)));
        btn.setPreferredSize(new Dimension(Math.max(btn.getPreferredSize().width, 144), 48));
        attachHover(btn, bg, soften(bg, 0.12f));
    }

    public static void styleSecondary(JButton btn, Color bg) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBackground(bg);
        btn.setForeground(getContrastingColor(bg));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(font(Font.BOLD, 13));
        btn.setUI(new RoundedButtonUI(BUTTON_ARC));
        btn.setBorder(new RoundedButtonBorder(softStroke(bg), BUTTON_ARC, new Insets(10, 18, 10, 18)));
        btn.setPreferredSize(new Dimension(Math.max(btn.getPreferredSize().width, 132), 44));
        attachHover(btn, bg, soften(bg, 0.10f));
    }

    public static void styleNavigationButton(AbstractButton button, Insets padding, int arc) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("buttonArc", arc);
        button.setUI(new RoundedButtonUI(arc));
        button.setBorder(new RoundedButtonBorder(new Color(255, 255, 255, 26), arc, padding));
    }

    public static void styleLinkButton(AbstractButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(PRIMARY);
        button.setFont(font(Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTextField(JTextComponent field) {
        field.setFont(font(Font.PLAIN, 15));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);
        field.setBackground(new Color(250, 252, 255));
        field.setOpaque(true);
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(207, 220, 236), 1, true),
                new EmptyBorder(11, 14, 11, 14)));
        Dimension size = field.getPreferredSize();
        field.setPreferredSize(new Dimension(Math.max(size.width, 220), 46));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static void styleTextArea(JTextArea area) {
        styleTextField(area);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setRows(4);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        area.setPreferredSize(new Dimension(Math.max(area.getPreferredSize().width, 220), 110));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(font(Font.PLAIN, 14));
        comboBox.setBackground(new Color(250, 252, 255));
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setOpaque(true);
        comboBox.setBorder(new LineBorder(new Color(207, 220, 236), 1, true));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        comboBox.setPreferredSize(new Dimension(Math.max(comboBox.getPreferredSize().width, 220), 46));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(font(Font.PLAIN, 14));
        spinner.setBackground(new Color(250, 252, 255));
        spinner.setOpaque(true);
        spinner.setBorder(new LineBorder(new Color(207, 220, 236), 1, true));
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        spinner.setPreferredSize(new Dimension(Math.max(spinner.getPreferredSize().width, 220), 46));
        spinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setFont(font(Font.PLAIN, 15));
            textField.setForeground(TEXT_PRIMARY);
            textField.setCaretColor(PRIMARY);
            textField.setBackground(new Color(250, 252, 255));
            textField.setBorder(new EmptyBorder(0, 12, 0, 12));
            textField.setHorizontalAlignment(JTextField.LEFT);
        }
    }

    public static void styleScrollPane(JScrollPane scrollPane, Color viewportColor) {
        scrollPane.setBorder(new RoundedButtonBorder(new Color(207, 220, 236), FIELD_ARC, new Insets(0, 0, 0, 0)));
        scrollPane.getViewport().setBackground(viewportColor);
        scrollPane.setBackground(viewportColor);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static void styleTable(JTable table) {
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(228, 235, 244));
        table.setSelectionBackground(PRIMARY_SOFT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFont(font(Font.PLAIN, 14));
        table.setRowHeight(34);
        table.setIntercellSpacing(new Dimension(0, 8));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(font(Font.BOLD, 13));
        header.setBackground(new Color(231, 239, 249));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
    }

    private static void attachHover(AbstractButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    private static Color getContrastingColor(Color bg) {
        double lum = 0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue();
        return lum < 145 ? Color.WHITE : TEXT_PRIMARY;
    }

    private static Color soften(Color color, float amount) {
        int red = color.getRed() + Math.round((255 - color.getRed()) * amount);
        int green = color.getGreen() + Math.round((255 - color.getGreen()) * amount);
        int blue = color.getBlue() + Math.round((255 - color.getBlue()) * amount);
        return new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue), color.getAlpha());
    }

    private static Color softStroke(Color base) {
        Color softened = soften(base, 0.35f);
        return new Color(softened.getRed(), softened.getGreen(), softened.getBlue(), 150);
    }

    private static final class RoundedPanel extends JPanel {
        private final Color fillColor;
        private final int arc;

        private RoundedPanel(LayoutManager layout, Color fillColor, int arc) {
            super(layout);
            this.fillColor = fillColor;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(13, 30, 53, 18));
            g2d.fillRoundRect(3, 6, getWidth() - 6, getHeight() - 8, arc, arc);
            g2d.setColor(fillColor);
            g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 10, arc, arc);
            g2d.setColor(BORDER);
            g2d.drawRoundRect(0, 0, getWidth() - 7, getHeight() - 11, arc, arc);
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    private static final class RoundedButtonUI extends BasicButtonUI {
        private final int defaultArc;

        private RoundedButtonUI(int defaultArc) {
            this.defaultArc = defaultArc;
        }

        @Override
        public void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = defaultArc;
            Object arcProperty = button.getClientProperty("buttonArc");
            if (arcProperty instanceof Integer) {
                arc = (Integer) arcProperty;
            }

            Color fill = button.getBackground();
            if (button.getModel().isPressed()) {
                fill = fill.darker();
            }

            g2d.setColor(fill);
            g2d.fillRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, arc, arc);
            g2d.dispose();

            super.paint(g, c);
        }
    }

    private static final class RoundedButtonBorder extends AbstractBorder {
        private final Color strokeColor;
        private final int arc;
        private final Insets insets;

        private RoundedButtonBorder(Color strokeColor, int arc, Insets insets) {
            this.strokeColor = strokeColor;
            this.arc = arc;
            this.insets = insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (strokeColor.getAlpha() > 0) {
                g2d.setColor(strokeColor);
                g2d.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
            }
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(insets.top, insets.left, insets.bottom, insets.right);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = this.insets.top;
            insets.left = this.insets.left;
            insets.bottom = this.insets.bottom;
            insets.right = this.insets.right;
            return insets;
        }
    }
}
