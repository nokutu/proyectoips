package ips.gui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Form class. You can add row to it and then get the results. Everything is nicely formatted.
 */
public class Form {

    private JPanel panel;
    private JPanel gridPanel;
    private List<Supplier<String>> values;

    private JTextPane errorPanel;

    private int line = 0;

    public Form() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());

        panel.add(gridPanel, BorderLayout.CENTER);
        values = new ArrayList<>();

        createErrorPanel();
        panel.add(errorPanel, BorderLayout.SOUTH);
    }

    private void createErrorPanel() {
        errorPanel = new JTextPane();
        errorPanel.setForeground(Color.red);
        errorPanel.setBackground(UIManager.getColor("Panel.background"));
        StyledDocument doc = errorPanel.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        errorPanel.setFocusable(false);
    }

    public void addLine(Component a, JTextField b) {
        doAddLine(a, b);
        values.add(b::getText);
    }

    public void addLine(Component a, JCheckBox b) {
        doAddLine(a, b);
        values.add(() -> String.valueOf(b.isSelected()));
    }

    public void addLine(Component a, JDateChooser b) {
        doAddLine(a, b);
        values.add(() -> String.valueOf(b.getDate().getTime()));
    }

    public void addLine(Component a, JComboBox<String> b) {
        addLine(a, b, true);
    }

    /**
     * Adds a {@link JComboBox} into the form.
     *
     * @param a the JLabel
     * @param b the JComboBox
     * @param returnContent true to return the content; false to return the index
     */
    public void addLine(Component a, JComboBox<String> b, boolean returnContent) {
        doAddLine(a, b);
        if (returnContent) {
            values.add(() -> b.getModel().getSelectedItem().toString());
        } else {
            values.add(() -> String.valueOf(b.getSelectedIndex()));
        }
    }

    public void addLine(Component a, JSpinner b) {
        doAddLine(a, b);
        values.add(() -> String.valueOf(b.getValue()));
    }

    private void doAddLine(Component a, Component b) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = line;
        c.insets = new Insets(2, 10, 2, 10);

        c.gridx = 0;
        gridPanel.add(a, c);

        c.gridx = 1;
        gridPanel.add(b, c);

        line++;
    }

    public JPanel getPanel() {
        return panel;
    }

    public List<String> getResults() {
        List<String> res = new ArrayList<>();
        for (Supplier<String> s : values) {
            res.add(s.get());
        }
        return res;
    }

    public void setError(String error) {
        errorPanel.setText(error.trim());
    }
}
