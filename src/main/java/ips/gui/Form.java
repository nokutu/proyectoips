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

/**
 * Form class. You can add row to it and then get the results. Everything is nicely formatted.
 */
public class Form {

    private JPanel panel;
    private JPanel gridPanel;
    private List<Supplier<String>> values;

    private JTextPane errorPanel;
    private JTextPane messagePanel;

    private int line = 0;
    private int nextInsetTop = 0;

    public Form() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());

        panel.add(gridPanel, c);
        values = new ArrayList<>();

        c.weightx = 1;

        c.gridy = 1;
        createErrorPanel();
        panel.add(errorPanel, c);

        c.gridy = 2;
        createMessagePanel();
        panel.add(messagePanel, c);
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

    private void createMessagePanel() {
        messagePanel = new JTextPane();
        messagePanel.setForeground(Color.green);
        messagePanel.setBackground(UIManager.getColor("Panel.background"));
        StyledDocument doc = messagePanel.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        messagePanel.setFocusable(false);
    }

    public void addSpace() {
        nextInsetTop += 10;
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
     * @param a             the JLabel
     * @param b             the JComboBox
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

    public void addLine(JCheckBox b) {
        addLine(b, false);
    }

    public void addLine(JCheckBox b, boolean addToValues) {
        doAddLine(b);
        if (addToValues) {
            values.add(() -> String.valueOf(b.isSelected()));
        }
    }

    private void doAddLine(Component a) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = line;
        c.insets = new Insets(2 + nextInsetTop, 10, 2, 10);
        nextInsetTop = 0;
        c.gridwidth = 2;

        c.gridx = 0;
        gridPanel.add(a, c);

        line++;
    }

    private void doAddLine(Component a, Component b) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = line;
        c.insets = new Insets(2 + nextInsetTop, 10, 2, 10);
        nextInsetTop = 0;

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
        messagePanel.setText("");
    }

    public void setMessage(String message) {
        messagePanel.setText(message);
        errorPanel.setText("");
    }
}
