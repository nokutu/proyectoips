package ips.gui;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
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
    private List<Supplier<String>> values;

    private int line = 0;

    public Form() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        values = new ArrayList<>();
    }

    public void addLine(Component a, JTextField b) {
        doAddLine(a, b);
        values.add(b::getText);
    }

    public void addLine(Component a, JDateChooser b) {
        doAddLine(a, b);
        values.add(() -> String.valueOf(b.getDate().getTime()));
    }

    public void addLine(Component a, JComboBox<String> b) {
        doAddLine(a, b);
        values.add(() -> b.getModel().getSelectedItem().toString());
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
        panel.add(a, c);

        c.gridx = 1;
        panel.add(b, c);

        line++;
    }

    public JPanel getPanel() {
        return panel;
    }

    public List<String> getResults() {
        return values.stream().map(Supplier::get).collect(Collectors.toList());
    }
}
